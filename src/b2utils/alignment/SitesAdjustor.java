package b2utils.alignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Given an alignment in a string map, return a BEAST2 Alignment,
 * which is split to variable sites and constant sites,
 * with weights of constant sites if required.
 * @author Walter Xie
 */
public class SitesAdjustor {

    final String id; // alignment
    final String totalcount; // for init Sequence
    final int stateLen = 1; // how many chars are 1 state in the sequence str
    final List<String> taxa;

    List<String> varSites = new ArrayList<>();
    List<String> constSites = new ArrayList<>();

    boolean ignoreUnknown = true;

    public SitesAdjustor(String id, String totalcount, Map<String, String> seqMap) {
        this.id = id;
        this.totalcount = totalcount;
        this.taxa = new ArrayList<>(seqMap.keySet());

        splitSites(seqMap);
    }

    private void splitSites(Map<String, String> seqMap) {
        for (Map.Entry<String, String> entry : seqMap.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());



        }


    }

    public void setIgnoreUnknown(boolean ignoreUnknown) {
        this.ignoreUnknown = ignoreUnknown;
    }


}
