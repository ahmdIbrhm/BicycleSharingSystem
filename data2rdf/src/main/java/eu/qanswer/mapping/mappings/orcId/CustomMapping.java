package eu.qanswer.mapping.mappings.orcId;

import eu.qanswer.mapping.configuration.Mapping;
import org.apache.jena.graph.Triple;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CustomMapping {
    Mapping mapping;
    public abstract ArrayList<Triple> function( HashMap<String,String> article, String key);
    public void setMapping(Mapping mapping)
    {
        this.mapping=mapping;
    }
    public Mapping getMapping()
    {
        return mapping;
    }
}
