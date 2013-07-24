package es.upm.cbgp.NCBITaxonomy2OWL;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class NCBITaxonomy2OWL {

	/**
	 * @param args
	 * @throws OWLOntologyCreationException 
	 * @throws OWLOntologyStorageException 
	 */
	public static void main(String[] args) throws OWLOntologyCreationException, OWLOntologyStorageException {
		String ncbi_taxon_ids_file_path = "";
		String API_KEY = "";  //Login to BioPortal (http://bioportal.bioontology.org/login) to get your API key 
//		String input_ontology_file_path = args [2]; 
//		String output_ontology_file_path = args [3]; 
		
		// Example taken from https://bmir-gforge.stanford.edu/gf/project/client_examples/
		// scmsvn/?action=browse&path=%2Ftrunk%2FJava%2FListOntologies-Java%2Fsrc%2FParseXMLResponse.java&view=markup
		
//		String NCBI_taxon = "obo:NCBITaxon_443906";
		
		NCBIOWLStore owl_store = new NCBIOWLStore();
		owl_store.loadModel("/home/mikel/git/NCBITaxonomy2OWL/test_data/test.owl");
		String id = "443906";
//		String id = "558270";
//		String id = "555";
//		String id = "5738";
		String NCBI_taxon = "obo:NCBITaxon_" +id;

		// TODO: Highly inefficient, it doesn't stop when a common ancestor is found! 
		for(;;){
			if(NCBI_taxon.equals("obo:NCBITaxon_1")){
				break;
			}
			else{
				owl_store = XMLResponseParser.parseXMLFile("http://rest.bioontology.org/bioportal/concepts/47845?conceptid="+ 
					NCBI_taxon + "&apikey=" +API_KEY, owl_store);
				NCBI_taxon = XMLResponseParser.getAddedLastTaxonID();
			}
		}
//		owl_store.addAnnotationAddedByNCBI2OWL("http://purl.obolibrary.org/obo/NCBITaxon_" + id);
		owl_store.writeModel("/home/mikel/git/NCBITaxonomy2OWL/test_data/test.owl");
	}
}
