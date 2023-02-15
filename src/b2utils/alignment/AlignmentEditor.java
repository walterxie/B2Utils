package b2utils.alignment;

import beast.base.evolution.alignment.Alignment;
import beast.base.evolution.alignment.Sequence;
import beast.base.parser.XMLProducer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Walter Xie
 */
public class AlignmentEditor {




    public static void main(String[] args) {

        String xmlFilePath = args[0];

        File xmlF = new File(xmlFilePath);

        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.parse(xmlF);
            doc.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException("Cannot find/parse XML file containing alignment " + xmlFilePath);
        }

        StringBuilder stringBuilder = new StringBuilder();

        // may multiple <data ... ></data>
        NodeList dataNodeList = doc.getElementsByTagName("data");
        for (int i = 0; i < dataNodeList.getLength(); i++) {
            // <data ... ></data>
            Node datNod = dataNodeList.item(i);

            if (datNod.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) datNod;
                String id = element.getAttribute("id");
                final String dt = element.getAttribute("dataType");

                System.out.println("\n<data id=" + id + " spec=" + element.getAttribute("spec") +
                        " dataType=" + dt);
                Alignment data = new Alignment();
                data.setID(id);

                // the keys in the order they were inserted
                Map<String, String> seqMap = new LinkedHashMap<>();
                String taxon;
                String totalcount = null;
                String value;
                NodeList seqNL = element.getElementsByTagName("sequence");
                for (int s = 0; s < seqNL.getLength(); s++) {
                    Element seqNod = (Element) seqNL.item(s);

//                    id = seqNod.getAttribute("id");
                    taxon = seqNod.getAttribute("taxon");
                    value = seqNod.getAttribute("value");
                    if (totalcount == null)
                        totalcount = seqNod.getAttribute("totalcount");
                    else if (!totalcount.equals(seqNod.getAttribute("totalcount")))
                        throw new IllegalArgumentException("totalcount should be same at " + taxon +
                                ", where " + seqNod.getAttribute("totalcount") + " != " + totalcount);

//                    Sequence seq = new Sequence(taxon, value);
                    seqMap.put(taxon, value);
                }
                System.out.println("Processing " + seqMap.size() + " sequences ...");

                //TODO modify seq
                List<Sequence> newSeqList = new ArrayList<>();

                Map varSites = getVariableSites(seqMap);



                // init Alignment
                data.initByName("sequence", seqList, "dataType", dt );

                String datXML = new XMLProducer().toXML(data);
                stringBuilder.append(datXML).append("\n");
            }
        }

        String outFP = xmlFilePath.replace(".xml", "-new.xml");
        File outF = new File(outFP);

        PrintWriter writer;
        try {
            FileWriter fileWriter = new FileWriter(outF);
            writer = new PrintWriter(fileWriter);
            writer.println(stringBuilder);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.close();
    }

}
