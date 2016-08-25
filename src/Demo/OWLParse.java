package Demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.DataRangeType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLProperty;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

class OWLParse{ 
	private static InputStream in;
	private static IRI physicalIRI;
	private static OWLOntologyManager manager;
	private static OWLDataFactory factory;
	private static OWLOntology ont;
	private static String defaultPrefix;
	private static PrefixManager prefix;
	//���캯����ʼ��
	 OWLParse(){
		in = FileManager.get().open("F:/code/OWL�ļ�/animal1.owl");
		physicalIRI = IRI.create("file:/F:/code/OWL�ļ�/animal1.owl");
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		try{
	    ont = manager.loadOntologyFromOntologyDocument(in);
		IRI documentIRI = manager.getOntologyDocumentIRI(ont);
		defaultPrefix = "http://www.semanticweb.org/ontologies/domain/2016/animal.owl#";
		prefix = new DefaultPrefixManager(defaultPrefix);
		
		}catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}
/*
		 **************************����********************************************
*/
	 //���ɸ���
	 public static OWLClass GetOWLClass(String string){
		 OWLClass concept = factory.getOWLClass(":"+string, prefix);
		 return concept;
	 }
	 
	 //����������
	 
	 public static Set<OWLClassExpression> AllClass(){
		 Set<OWLClassExpression> set = new HashSet<OWLClassExpression>();
		 for( OWLClass cls: ont.getClassesInSignature()){
				set.add(cls);
			} 
		return set;
	 }
	//�õ����ڵ㼯
	public static Set<OWLClassExpression> GetParents(OWLClass concept){
		OWLClass sub = concept;
		Set<OWLClassExpression> superclass = sub.getSuperClasses(ont);
		return superclass;
	}
	//�õ��ӽڵ㼯
	public static Set<OWLClassExpression> GetChildren(OWLClass concept){
		Set<OWLClassExpression> subclass = concept.getSubClasses(ont);
		
		return subclass;
	}
	//��ȡ����ʵ��
	public static Set<OWLIndividual> IndividualOfClass(OWLClass concept){
		Set<OWLIndividual> individuals = concept.getIndividuals(ont);
		return individuals;
	}
	//��ȡ�����������
		public static Set<OWLObjectProperty> ObjectPropertyOfClass(){
			Set<OWLObjectProperty> op = ont.getObjectPropertiesInSignature();
			return op;
		}
	//��ȡ�����������
	public static Set<OWLDataProperty> DataPropertyOfClass(OWLClass concept){
		Set<OWLDataProperty> dp = concept.getDataPropertiesInSignature();
		return dp;
	}

	 //�������Ƚڵ�
	public static Set<OWLClassExpression> FindAncestor(OWLClass ontconcept){
		
		Set<OWLClassExpression> super1 = ontconcept.getSuperClasses(ont);
		Set<OWLClassExpression> parent = null;
		//��set��תΪlist��
		Iterator<OWLClassExpression> iterator1 = super1.iterator();
		ArrayList<OWLClass> list = new ArrayList<OWLClass>();		
		while(iterator1.hasNext()){
			OWLClass sub = (OWLClass) iterator1.next();
			list.add(sub);
		}
		//��list���еݹ��ѯ����
		for (int i = 0; i < list.size(); i++){
			parent = ((OWLClass) list.get(i)).getSuperClasses(ont);
			if(!parent.isEmpty()){
			Iterator<OWLClassExpression> it = parent.iterator();
			while(it.hasNext()){
				OWLClass sub = (OWLClass) it.next();
				list.add(sub);
			}
			}
		}
		//�ڵ����ȱ��浽set����
		super1.clear();
		for(OWLClass c:list){
			super1.add(c);
		}
		return super1;	
	}
	
	//��Ӹ��ӹ���
	public static void AddParentAxiom(OWLClass child,OWLClass parent){
		OWLAxiom axiom = factory.getOWLSubClassOfAxiom(child,parent);
		//��������
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//ɾ�����ӹ���
	public static void RemoveParentAxiom(OWLClass child,OWLClass parent){
		Set<OWLClassExpression> sup = child.getSuperClasses(ont);
		if(sup.size() == 1){
			RemoveConcept(child);
		}
		OWLAxiom axiom = factory.getOWLSubClassOfAxiom(child,parent);
		manager.removeAxiom(ont, axiom);
	}
	//ɾ������
	public static void RemoveConcept(OWLClass concept) {
			Set<OWLOntology> set = new HashSet<OWLOntology>();
	        set.add(ont);
			OWLEntityRemover remover = new OWLEntityRemover(manager, set);
	        concept.accept(remover);
	        manager.applyChanges(remover.getChanges());
		}
	
/*
	 **************************ʵ��********************************************
 */
	
	//���ʵ����
	public static OWLIndividual GetIndividual(String string){
		OWLIndividual instance = factory.getOWLNamedIndividual(string, prefix);
		 return instance;
	}

	//�������ʵ��
	public static Set<OWLNamedIndividual> AllIndividual(){
		Set<OWLNamedIndividual> instance = ont.getIndividualsInSignature();
		return instance;
	}
	//���ʵ������������
	public static Set<OWLClassExpression> ClassOfIndividual(OWLIndividual instance){
		Set<OWLClassExpression> concept = instance.getTypes(ont);
		return concept;
	}
	//���ʵ������
	public static void AddInstance(OWLIndividual instance,OWLClass concept){
		
		OWLIndividualAxiom axiom = factory.getOWLClassAssertionAxiom(concept, instance);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//ɾ��ʵ������
	public static void RemoveInstance(OWLIndividual instance,OWLClass concept){
		
		OWLIndividualAxiom axiom = factory.getOWLClassAssertionAxiom(concept, instance);
		RemoveAxiom removeaxiom = new RemoveAxiom(ont, axiom);
		manager.applyChange(removeaxiom);
	}
	//ɾ��ʵ��
	public static void RemoveIndividual(OWLIndividual instance){
		OWLNamedIndividual in = instance.asOWLNamedIndividual();
		Set<OWLOntology> set = new HashSet<OWLOntology>();
        set.add(ont);
		OWLEntityRemover remover = new OWLEntityRemover(manager, set);
        in.accept(remover);
        manager.applyChanges(remover.getChanges());
	}
/*
	 **************************����********************************************
*/
	//��ö���������
	public static OWLObjectProperty ObjectProperty(String string){
		OWLObjectProperty op = factory.getOWLObjectProperty(string, prefix);
		return op;
	}
	//�������������
	public static OWLDataProperty DataProperty(String string){
		OWLDataProperty op = factory.getOWLDataProperty(string, prefix);
		return op;
		}
	//�����������
	public static Set<OWLObjectProperty> AllObjectProperty(){
		Set<OWLObjectProperty> op = ont.getObjectPropertiesInSignature();
		return op;
	}
	public static Set<OWLDataProperty> AllDataProperty(){
		Set<OWLDataProperty> dp = ont.getDataPropertiesInSignature();
		return dp;
	}
	//��ø�����
	public static Set<OWLObjectPropertyExpression> ParentObjectProperty(OWLObjectProperty op){
		Set<OWLObjectPropertyExpression> parent = op.getSuperProperties(ont);
		return parent;
	}
	public static Set<OWLDataPropertyExpression> ParentDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> parent = dp.getSuperProperties(ont);
		return parent;
	}
	//���������
	public static Set<OWLObjectPropertyExpression> ChildObjectProperty(OWLObjectProperty op){
		Set<OWLObjectPropertyExpression> child = op.getSubProperties(ont);
		return child;
	}
	public static Set<OWLDataPropertyExpression> ChildDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> child = dp.getSubProperties(ont);
		return child;
	}
	//�����������
	public static Set<OWLObjectPropertyExpression> AncestorObjectProperty(OWLObjectProperty subop){
		Set<OWLObjectPropertyExpression> super1 = subop.getSuperProperties(ont);
		Set<OWLObjectPropertyExpression> parent = null;
		//��set��תΪlist��
		Iterator<OWLObjectPropertyExpression> iterator1 = super1.iterator();
		ArrayList<OWLObjectProperty> list = new ArrayList<OWLObjectProperty>();		
		while(iterator1.hasNext()){
			OWLObjectProperty sub = (OWLObjectProperty) iterator1.next();
			list.add(sub);
		}
		//��list���еݹ��ѯ����
		for (int i = 0; i < list.size(); i++){
			parent = (list.get(i)).getSuperProperties(ont);
			if(!parent.isEmpty()){
			Iterator<OWLObjectPropertyExpression> it = parent.iterator();
			while(it.hasNext()){
				OWLObjectProperty sub =  (OWLObjectProperty) it.next();
				list.add(sub);
			}
			}
		}
		//�ڵ����ȱ��浽set����
		super1.clear();
		for(OWLObjectProperty c:list){
			super1.add(c);
		}
		return super1;
	}
	public static Set<OWLDataPropertyExpression> AncestorDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> super1 = dp.getSuperProperties(ont);
		Set<OWLDataPropertyExpression> parent = null;
		//��set��תΪlist��
		Iterator<OWLDataPropertyExpression> iterator1 = super1.iterator();
		ArrayList<OWLDataProperty> list = new ArrayList<OWLDataProperty>();		
		while(iterator1.hasNext()){
			OWLDataProperty sub = (OWLDataProperty) iterator1.next();
			list.add(sub);
		}
		//��list���еݹ��ѯ����
		for (int i = 0; i < list.size(); i++){
			parent = (list.get(i)).getSuperProperties(ont);
			if(!parent.isEmpty()){
			Iterator<OWLDataPropertyExpression> it = parent.iterator();
			while(it.hasNext()){
				OWLDataProperty sub =  (OWLDataProperty) it.next();
				list.add(sub);
			}
			}
		}
		//�ڵ����ȱ��浽set����
		super1.clear();
		for(OWLDataProperty c:list){
			super1.add(c);
		}
		return super1;
	}
	//��ȡ����
	public static Set<OWLClassExpression> ObjectPropertyDomain(OWLObjectProperty op){
		Set<OWLClassExpression> domain = op.getDomains(ont);
		return domain;
	}
	public static Set<OWLClassExpression> DatatPropertyDomain(OWLDataProperty dp){
		Set<OWLClassExpression> domain = dp.getDomains(ont);
		dp.getRanges(ont);
		return domain;
	}
	//��ȡֵ��
	public static Set<OWLClassExpression> ObjectPropertyRange(OWLObjectProperty op){
		Set<OWLClassExpression> range = op.getRanges(ont);
		return range;
	}
	public static Set<OWLDataRange> DatatPropertyRange(OWLDataProperty dp){
		Set<OWLDataRange> range = dp.getRanges(ont);
		return range;
	}
	//���ʵ����������Զ���
	public static void AddObjectProperty(OWLObjectProperty op,OWLIndividual i1,OWLIndividual i2){
		OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(op,i1,i2);
		manager.addAxiom(ont, axiom);
	}
	//���ʵ�����������Զ���
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,boolean b){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, b);
		manager.addAxiom(ont, axiom);
	}
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,String s){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, s);
		manager.addAxiom(ont, axiom);
	}
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,int i){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, i);
		manager.addAxiom(ont, axiom);
	}
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,double d){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, d);
		manager.addAxiom(ont, axiom);
	}
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,float f){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, f);
		manager.addAxiom(ont, axiom);
	}
	public static void AddDataProperty(OWLDataProperty dp,OWLIndividual i1,OWLLiteral l){
		OWLAxiom axiom = factory.getOWLDataPropertyAssertionAxiom(dp, i1, l);
		manager.addAxiom(ont, axiom);
	}

	//��Ӷ������Զ�����
	public static void SetOPDomain(OWLObjectProperty op, OWLClass c){
		OWLAxiom axiom = factory.getOWLObjectPropertyDomainAxiom(op, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//��Ӷ�������ֵ��
	public static void SetOPRange(OWLObjectProperty op, OWLClass c){
		OWLAxiom axiom = factory.getOWLObjectPropertyRangeAxiom(op, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//����������Զ�����
	public static void SetDPDomain(OWLDataProperty dp, OWLClass c){
		OWLAxiom axiom = factory.getOWLDataPropertyDomainAxiom(dp, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom); 
	}
	//��ȡowl��������
	public static OWLDataRange IntegerDataType(){
		// For common data types there are some convenience methods of
        // OWLDataFactory
		OWLDataRange r = factory.getIntegerOWLDatatype();
		return r;
	
	}
	public static OWLDataRange StringDataType(){
		// OWLDatatype represents named datatypes in OWL
        // The OWL2Datatype enum defines built in OWL 2 Datatypes
		OWLDataRange r = factory.getOWLDatatype(OWL2Datatype.XSD_STRING.getIRI());
		return r;
	}
	public static OWLDataRange BooleanDataType(){
		OWLDataRange r = factory.getBooleanOWLDatatype();
		return r;
	}
	public static OWLDataRange DoubleDataType(){
		OWLDataRange r = factory.getDoubleOWLDatatype();
		
		return r;
	}
	public static OWLDataRange FloatDataType(){
		OWLDataRange r = factory.getFloatOWLDatatype();
		return r;
	}
	public static OWLDataRange RDFPlainLiteralDataType(){
		OWLDataRange r = factory.getRDFPlainLiteral();
		return r;
	}
	//�����������ֵ��
	public static void SetDPRange(OWLDataProperty dp, OWLDataRange r){
		OWLAxiom axiom = factory.getOWLDataPropertyRangeAxiom(dp, r);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
		
	}
	//��Ӷ���������
	public static void AddParentOfObjectProperty(OWLObjectProperty op1,OWLObjectProperty op2){
		OWLAxiom axiom = factory.getOWLSubObjectPropertyOfAxiom(op1, op2);
		manager.addAxiom(ont, axiom);
	}
	//�������������
	public static void AddParentOfDataProperty(OWLDataProperty dp1,OWLDataProperty dp2){
		OWLAxiom axiom = factory.getOWLSubDataPropertyOfAxiom(dp1, dp2);
		manager.addAxiom(ont, axiom);
	}	
	
	//ɾ���������Թ���
	public static void RemoveObjectProperty(OWLObjectProperty op1,OWLObjectProperty op2){
		OWLAxiom axiom = factory.getOWLSubObjectPropertyOfAxiom(op1, op2);
		RemoveAxiom removeaxiom = new RemoveAxiom(ont, axiom);
		manager.applyChange(removeaxiom);
	}
	//ɾ���������Թ���
	public static void RemoveDataProperty(OWLDataProperty dp1,OWLDataProperty dp2){
		OWLAxiom axiom = factory.getOWLSubDataPropertyOfAxiom(dp1, dp2);
		manager.removeAxiom(ont, axiom);
	}
	//ɾ������
	public static void RemoveObjectProperty(OWLObjectProperty op){
		Set<OWLOntology> set = new HashSet<OWLOntology>();
        set.add(ont);
		OWLEntityRemover remover = new OWLEntityRemover(manager, set);
        op.accept(remover);
        manager.applyChanges(remover.getChanges());
	}
	public static OWLObjectProperty TopObjectProperty(){
		OWLObjectProperty top  = factory.getOWLTopObjectProperty();
		return top;
	}
	public static OWLDataProperty TopDataProperty(){
		OWLDataProperty top  = factory.getOWLTopDataProperty();
		return top;
	}
	//����OWL�ļ�
	public static void Save(){
		try {
			manager.saveOntology(ont, physicalIRI);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}