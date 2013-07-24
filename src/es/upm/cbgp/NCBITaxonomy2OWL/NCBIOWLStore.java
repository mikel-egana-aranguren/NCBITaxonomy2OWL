package es.upm.cbgp.NCBITaxonomy2OWL;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class NCBIOWLStore {
	private OWLOntologyManager manager = null;
	private OWLDataFactory factory = null;
	private OWLOntology ontology = null;
	public NCBIOWLStore(){
		manager = OWLManager.createOWLOntologyManager(); 
		factory = manager.getOWLDataFactory();
	}
	public void loadModel(String filePath) throws OWLOntologyCreationException{
//		"/home/mikel/git/NCBITaxonomy2OWL/test_new.owl"
		ontology = manager.loadOntologyFromOntologyDocument(new File(filePath));
	}
	public void writeModel(String filePath) throws OWLOntologyStorageException{
//		"/home/mikel/git/NCBITaxonomy2OWL/test_new_new.owl"
		File out_owl_file = new File(filePath);
		RDFXMLOntologyFormat rdfxmlformat = new RDFXMLOntologyFormat();
		manager.saveOntology(ontology,rdfxmlformat, IRI.create(out_owl_file.toURI()));
	}
	public void addTaxon(String childFullID, String parentFullID){
		OWLClass taxonOWLClass = factory.getOWLClass(IRI.create(childFullID));
		OWLClass taxonParentOWLClass = factory.getOWLClass(IRI.create(parentFullID));
		OWLAxiom axiom = factory.getOWLSubClassOfAxiom(taxonOWLClass, taxonParentOWLClass);
		AddAxiom addAxiom = new AddAxiom(ontology, axiom);
		manager.applyChange(addAxiom);
	}
	public void addLabel(String taxonFullId, String label){
		OWLAnnotation labelAnno = factory.getOWLAnnotation(
				factory.getRDFSLabel(),
				factory.getOWLLiteral(label, "en"));
		OWLClass cls = factory.getOWLClass(IRI.create(taxonFullId));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), labelAnno);
		manager.applyChange(new AddAxiom(ontology, ax));
	}
	public void addAnnotationAddedByNCBI2OWL(String taxonFullId){
		OWLAnnotation labelAnno = factory.getOWLAnnotation(
				factory.getRDFSLabel(),
				factory.getOWLLiteral("AddedByNCBI2OWL", "en"));
		OWLClass cls = factory.getOWLClass(IRI.create(taxonFullId));
		OWLAxiom ax = factory.getOWLAnnotationAssertionAxiom(cls.getIRI(), labelAnno);
		manager.applyChange(new AddAxiom(ontology, ax));
	}
	public boolean termExists(String fullId){
		System.out.println(fullId);
		System.out.println(ontology.containsClassInSignature(IRI.create(fullId)));
		return ontology.containsClassInSignature(IRI.create(fullId));
	}
}
