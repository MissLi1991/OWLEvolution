package Demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.semanticweb.owlapi.util.SimpleIRIMapper;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;

class OWLParse{ 
	private static InputStream in;
	private static InputStream in1;
	private static InputStream in2;
	private static IRI physicalIRI;
	private static OWLOntologyManager manager;
	private static OWLDataFactory factory;
	private static OWLOntology ont;
	private static String defaultPrefix;
	private static PrefixManager prefix;
	//构造函数初始化
	 OWLParse(){

		in = FileManager.get().open("F:/code/OWL文件/pet1.owl");
		manager = OWLManager.createOWLOntologyManager();
		factory = manager.getOWLDataFactory();
		try{
		
		//映射import的human1本体
		IRI ontologyIRI1 = IRI.create("http://www.semanticweb.org/ontologies/domain/2016/human2.owl");
		IRI physicalIRI1 = IRI.create("file:/F:/code/OWL文件/human2.owl");
		SimpleIRIMapper mapper1 = new SimpleIRIMapper(ontologyIRI1, physicalIRI1);
		manager.addIRIMapper(mapper1);
		
		//映射import的animal本体
		IRI ontologyIRI2 = IRI.create("http://www.semanticweb.org/ontologies/domain/2016/animal.owl");
		IRI physicalIRI2 = IRI.create("file:/F:/code/OWL文件/animal.owl");
		SimpleIRIMapper mapper2 = new SimpleIRIMapper(ontologyIRI2, physicalIRI2);
		manager.addIRIMapper(mapper2);
		
		IRI ontologyIRI = IRI.create("http://www.semanticweb.org/ontologies/application/2016/pet1.owl");
		physicalIRI = IRI.create("file:/F:/code/OWL文件/pet1.owl");
		SimpleIRIMapper mapper = new SimpleIRIMapper(ontologyIRI, physicalIRI);
        manager.addIRIMapper(mapper);
        
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
		 **************************概念********************************************
*/
	 //生成概念
	 public static OWLClass GetOWLClass(String string){
		 OWLClass concept = factory.getOWLClass(":"+string, prefix);
		 return concept;
	 }
	 
	 //查找所有类
	 
	 public static Set<OWLClassExpression> AllClass(){
		 Set<OWLClassExpression> set = new HashSet<OWLClassExpression>();
		 for( OWLClass cls: ont.getClassesInSignature()){
				set.add(cls);
			} 
		return set;
	 }
	//得到父节点集
	public static Set<OWLClassExpression> GetParents(OWLClass concept){
		OWLClass sub = concept;
		Set<OWLClassExpression> superclass = sub.getSuperClasses(ont);
		return superclass;
	}
	//获取父类集合
	public static Set<OWLClassExpression> Parents(OWLClass concept){
		OWLClass sub = concept;
		Set<OWLClassExpression> superclass = sub.getSuperClasses(ont);
		Iterator<OWLClassExpression> it = superclass.iterator();
		while(it.hasNext()){
			OWLClass c = (OWLClass) it.next();
			Set<OWLClassExpression> parent = GetParents(c);
			superclass.addAll(parent);
		}
		return superclass;
		
	}
	//得到子节点集合
	public static Set<OWLClassExpression> GetChildren(OWLClass concept){
		Set<OWLClassExpression> subclass = concept.getSubClasses(ont);
		
		return subclass;
	}
	//获取子类集合（两级深度）
	public static Set<OWLClassExpression> Childs(OWLClass concept){
		OWLClass sup = concept;
		Set<OWLClassExpression> subclass = sup.getSubClasses(ont);
		Iterator<OWLClassExpression> it = subclass.iterator();
		while(it.hasNext()){
			OWLClass c = (OWLClass) it.next();
			Set<OWLClassExpression> child = GetChildren(c);
			subclass.addAll(child);
		}
		return subclass;
		
	}
	//获取兄弟概念集合
	public static Set<OWLClassExpression> Siblings(OWLClass concept){
		OWLClass sub = concept;
		Set<OWLClassExpression> sibling = new HashSet<>();
		Set<OWLClassExpression> superclass = sub.getSuperClasses(ont);
		Iterator<OWLClassExpression> it = superclass.iterator();
		while(it.hasNext()){
			OWLClass c = (OWLClass) it.next();
			Set<OWLClassExpression> child = GetChildren(c);
			Iterator<OWLClassExpression> it1 = child.iterator();
			while(it1.hasNext()){
				OWLClass ch = (OWLClass) it1.next();
				if(!ch.equals(sub)){
					sibling.add(ch);
				}
			}
		}
		return sibling;
	}
	//获取概念实例
	public static Set<OWLIndividual> IndividualOfClass(OWLClass concept){
		Set<OWLIndividual> individuals = concept.getIndividuals(ont);
		return individuals;
	}
	/*查不到概念的对象属性和数据属性
	//获取概念对象属性
		public static Set<OWLObjectProperty> ObjectPropertyOfClass(){
			Set<OWLObjectProperty> op = ont.getObjectPropertiesInSignature();
			return op;
		}
	//获取概念对象属性
	public static Set<OWLDataProperty> DataPropertyOfClass(OWLClass concept){
		Set<OWLDataProperty> dp = concept.getDataPropertiesInSignature();
		return dp;
	}
	*/

	//查找祖先节点
	public static Set<OWLClassExpression> FindAncestor(OWLClass ontconcept){
	
		
		Set<OWLClassExpression> super1 = ontconcept.getSuperClasses(ont);
		Set<OWLClassExpression> parent = null;
		//将set集转为list集
		Iterator<OWLClassExpression> iterator1 = super1.iterator();
		ArrayList<OWLClass> list = new ArrayList<OWLClass>();		
		while(iterator1.hasNext()){
			OWLClass sub = (OWLClass) iterator1.next();
			list.add(sub);
		}
		//在list表中递归查询父类
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
		//节点祖先保存到set集中
		super1.clear();
		for(OWLClass c:list){
			super1.add(c);
		}
		return super1;	
	}
	
	//添加父子公理
	public static void AddParentAxiom(OWLClass child,OWLClass parent){
		OWLAxiom axiom = factory.getOWLSubClassOfAxiom(child,parent);
		//创建公理：
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//删除父子公理
	public static void RemoveParentAxiom(OWLClass child,OWLClass parent){
		Set<OWLClassExpression> sup = child.getSuperClasses(ont);
		if(sup.size() == 1){
			RemoveConcept(child);
		}
		OWLAxiom axiom = factory.getOWLSubClassOfAxiom(child,parent);
		manager.removeAxiom(ont, axiom);
	}
	//删除概念
	public static void RemoveConcept(OWLClass concept) {
			Set<OWLOntology> set = new HashSet<OWLOntology>();

	        set.add(ont);
			OWLEntityRemover remover = new OWLEntityRemover(manager, set);
	        concept.accept(remover);
	        manager.applyChanges(remover.getChanges());
		}
	
/*
	 **************************实例********************************************
 */
	
	//获得实例名
	public static OWLIndividual GetIndividual(String string){
		OWLIndividual instance = factory.getOWLNamedIndividual(string, prefix);
		 return instance;
	}

	//获得所有实例
	public static Set<OWLNamedIndividual> AllIndividual(){
		Set<OWLNamedIndividual> instance = ont.getIndividualsInSignature();
		return instance;
	}
	//获得实例的数据属性值
	public static HashMap DPofInstance(OWLIndividual i){
		HashMap map  = new HashMap<>();
		map = (HashMap) i.getDataPropertyValues(ont);
		return map;
	}
	//获得实例的对象属性值
	public static HashMap OPofInstance(OWLIndividual i){
		HashMap map  = new HashMap<>();
		map = (HashMap) i.getObjectPropertyValues(ont);
		return map;
	}
	//获得实例的所属概念
	public static Set<OWLClassExpression> ClassOfIndividual(OWLIndividual instance){
		Set<OWLClassExpression> concept = instance.getTypes(ont);
		return concept;
	}
	//添加实例公理
	public static void AddInstance(OWLIndividual instance,OWLClass concept){
		
		OWLIndividualAxiom axiom = factory.getOWLClassAssertionAxiom(concept, instance);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//删除实例公理
	public static void RemoveInstance(OWLIndividual instance,OWLClass concept){
		
		OWLIndividualAxiom axiom = factory.getOWLClassAssertionAxiom(concept, instance);
		RemoveAxiom removeaxiom = new RemoveAxiom(ont, axiom);
		manager.applyChange(removeaxiom);
	}
	//删除实例
	public static void RemoveIndividual(OWLIndividual instance){
		OWLNamedIndividual in = instance.asOWLNamedIndividual();
		Set<OWLOntology> set = new HashSet<OWLOntology>();
        set.add(ont);
		OWLEntityRemover remover = new OWLEntityRemover(manager, set);
        in.accept(remover);
        manager.applyChanges(remover.getChanges());
	}
/*
	 **************************属性********************************************
*/
	//获得对象属性名
	public static OWLObjectProperty ObjectProperty(String string){
		OWLObjectProperty op = factory.getOWLObjectProperty(string, prefix);
		return op;
	}
	//获得数据属性名
	public static OWLDataProperty DataProperty(String string){
		OWLDataProperty op = factory.getOWLDataProperty(string, prefix);
		return op;
		}
	//获得所有属性
	public static Set<OWLObjectProperty> AllObjectProperty(){
		Set<OWLObjectProperty> op = ont.getObjectPropertiesInSignature();
		return op;
	}
	public static Set<OWLDataProperty> AllDataProperty(){
		Set<OWLDataProperty> dp = ont.getDataPropertiesInSignature();
		return dp;
	}
	//获得父属性
	public static Set<OWLObjectPropertyExpression> ParentObjectProperty(OWLObjectProperty op){
		Set<OWLObjectPropertyExpression> parent = op.getSuperProperties(ont);
		return parent;
	}
	public static Set<OWLDataPropertyExpression> ParentDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> parent = dp.getSuperProperties(ont);
		return parent;
	}
	//获得子属性
	public static Set<OWLObjectPropertyExpression> ChildObjectProperty(OWLObjectProperty op){
		Set<OWLObjectPropertyExpression> child = op.getSubProperties(ont);
		return child;
	}
	public static Set<OWLDataPropertyExpression> ChildDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> child = dp.getSubProperties(ont);
		return child;
	}
	//获得祖先属性
	public static Set<OWLObjectPropertyExpression> AncestorObjectProperty(OWLObjectProperty subop){
		Set<OWLObjectPropertyExpression> super1 = subop.getSuperProperties(ont);
		Set<OWLObjectPropertyExpression> parent = null;
		//将set集转为list集
		Iterator<OWLObjectPropertyExpression> iterator1 = super1.iterator();
		ArrayList<OWLObjectProperty> list = new ArrayList<OWLObjectProperty>();		
		while(iterator1.hasNext()){
			OWLObjectProperty sub = (OWLObjectProperty) iterator1.next();
			list.add(sub);
		}
		//在list表中递归查询父类
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
		//节点祖先保存到set集中
		super1.clear();
		for(OWLObjectProperty c:list){
			super1.add(c);
		}
		return super1;
	}
	public static Set<OWLDataPropertyExpression> AncestorDataProperty(OWLDataProperty dp){
		Set<OWLDataPropertyExpression> super1 = dp.getSuperProperties(ont);
		Set<OWLDataPropertyExpression> parent = null;
		//将set集转为list集
		Iterator<OWLDataPropertyExpression> iterator1 = super1.iterator();
		ArrayList<OWLDataProperty> list = new ArrayList<OWLDataProperty>();		
		while(iterator1.hasNext()){
			OWLDataProperty sub = (OWLDataProperty) iterator1.next();
			list.add(sub);
		}
		//在list表中递归查询父类
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
		//节点祖先保存到set集中
		super1.clear();
		for(OWLDataProperty c:list){
			super1.add(c);
		}
		return super1;
	}
	//获取定义域集
	public static Set<OWLClassExpression> ObjectPropertyDomain(OWLObjectProperty op){
		Set<OWLClassExpression> domain = op.getDomains(ont);
		return domain;
	}
	public static Set<OWLClassExpression> DatatPropertyDomain(OWLDataProperty dp){
		Set<OWLClassExpression> domain = dp.getDomains(ont);
		dp.getRanges(ont);
		return domain;
	}
	//获取值域集
	public static Set<OWLClassExpression> ObjectPropertyRange(OWLObjectProperty op){
		Set<OWLClassExpression> range = op.getRanges(ont);
		return range;
	}
	public static Set<OWLDataRange> DatatPropertyRange(OWLDataProperty dp){
		Set<OWLDataRange> range = dp.getRanges(ont);
		return range;
	}
	//添加实例间对象属性断言
	public static void AddObjectProperty(OWLObjectProperty op,OWLIndividual i1,OWLIndividual i2){
		OWLAxiom axiom = factory.getOWLObjectPropertyAssertionAxiom(op,i1,i2);
		manager.addAxiom(ont, axiom);
	}
	//添加实例间数据属性断言
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

	//添加对象属性定义域
	public static void SetOPDomain(OWLObjectProperty op, OWLClass c){
		OWLAxiom axiom = factory.getOWLObjectPropertyDomainAxiom(op, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//添加对象属性值域
	public static void SetOPRange(OWLObjectProperty op, OWLClass c){
		OWLAxiom axiom = factory.getOWLObjectPropertyRangeAxiom(op, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
	}
	//添加数据属性定义域
	public static void SetDPDomain(OWLDataProperty dp, OWLClass c){
		OWLAxiom axiom = factory.getOWLDataPropertyDomainAxiom(dp, c);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom); 
	}
	//获取owl数据类型
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
	//添加数据属性值域
	public static void SetDPRange(OWLDataProperty dp, OWLDataRange r){
		OWLAxiom axiom = factory.getOWLDataPropertyRangeAxiom(dp, r);
		AddAxiom addaxiom = new AddAxiom(ont, axiom);
		manager.applyChange(addaxiom);
		
	}
	//添加对象子属性
	public static void AddParentOfObjectProperty(OWLObjectProperty op1,OWLObjectProperty op2){
		OWLAxiom axiom = factory.getOWLSubObjectPropertyOfAxiom(op1, op2);
		manager.addAxiom(ont, axiom);
	}
	//添加数据子属性
	public static void AddParentOfDataProperty(OWLDataProperty dp1,OWLDataProperty dp2){
		OWLAxiom axiom = factory.getOWLSubDataPropertyOfAxiom(dp1, dp2);
		manager.addAxiom(ont, axiom);
	}	
	
	//删除对象属性公理
	public static void RemoveObjectProperty(OWLObjectProperty op1,OWLObjectProperty op2){
		OWLAxiom axiom = factory.getOWLSubObjectPropertyOfAxiom(op1, op2);
		RemoveAxiom removeaxiom = new RemoveAxiom(ont, axiom);
		manager.applyChange(removeaxiom);
	}
	//删除数据属性公理
	public static void RemoveDataProperty(OWLDataProperty dp1,OWLDataProperty dp2){
		OWLAxiom axiom = factory.getOWLSubDataPropertyOfAxiom(dp1, dp2);
		manager.removeAxiom(ont, axiom);
	}
	//删除属性
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
	//保存OWL文件
	public static void Save(){
		try {
			manager.saveOntology(ont, physicalIRI);
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*
 * ********************************获取概念名称（不包含前缀）******************************
 */
	//获取概念名称
	public static String Concept(OWLClass c){
		String s = c.toString();
    	s = s.substring(s.lastIndexOf("#")+1,s.lastIndexOf(">"));
		return s;
	}
	public static String Concept(OWLIndividual i){
		String s = i.toString();
    	s = s.substring(s.lastIndexOf("#")+1,s.lastIndexOf(">"));
		return s;
	}
	public static String Concept(OWLDataProperty dp){
		String s = dp.toString();
    	s = s.substring(s.lastIndexOf("#")+1,s.lastIndexOf(">"));
		return s;
	}
	public static String Concept(OWLObjectProperty op){
		String s = op.toString();
    	s = s.substring(s.lastIndexOf("#")+1,s.lastIndexOf(">"));
		return s;
	}
	public static ArrayList<String> Concept(Set<OWLClassExpression> set){
		ArrayList<String> arraylist = new ArrayList<>();
		Iterator<OWLClassExpression> it = set.iterator();
		while(it.hasNext()){
			OWLClass sup = (OWLClass) it.next();
			String s = Concept(sup);
			arraylist.add(s);
		}
		return arraylist;
	}
	public static ArrayList<String> Instance(Set<OWLIndividual> set){
		ArrayList<String> arraylist = new ArrayList<>();
		Iterator<OWLIndividual> it = set.iterator();
		while(it.hasNext()){
			OWLIndividual sup = (OWLIndividual) it.next();
			String s = Concept(sup);
			arraylist.add(s);
		}
		return arraylist;
	}
	public static ArrayList<String> DataProperty(Set<OWLDataProperty> set){
		ArrayList<String> arraylist = new ArrayList<>();
		Iterator<OWLDataProperty> it = set.iterator();
		while(it.hasNext()){
			OWLDataProperty dp = (OWLDataProperty) it.next();
			String s = Concept(dp);
			arraylist.add(s);
		}
		return arraylist;
	}	
	public static ArrayList<String> ObjectProperty(Set<OWLObjectProperty> set){
		ArrayList<String> arraylist = new ArrayList<>();
		Iterator<OWLObjectProperty> it = set.iterator();
		while(it.hasNext()){
			OWLObjectProperty op = (OWLObjectProperty) it.next();
			System.out.println(op);
			if(op.toString().startsWith("<")){
				String s = Concept(op);
				arraylist.add(s);
				}
			
		}
		return arraylist;
	}
	//获取父类集合
	public static ArrayList<String> Parent(OWLClass c){
		Set<OWLClassExpression> parent = Parents(c);
		return Concept(parent);
	}
	//获取子类集合
	public static ArrayList<String> Child(OWLClass c){
		Set<OWLClassExpression> child = Childs(c);
		return Concept(child);
	}
	//获取兄弟集合
	public static ArrayList<String> Sibling(OWLClass c){
		Set<OWLClassExpression> sibling = Siblings(c);
		return Concept(sibling);
	}
	//获取实例
	public static ArrayList<String> Individual(OWLClass c){
		Set<OWLIndividual> individual =IndividualOfClass(c);
		return Instance(individual);
	}
	//数据属性
	public static ArrayList<String> DPKeys(OWLIndividual i){

		HashMap map  = new HashMap<>();
		ArrayList<String> arraylist = new ArrayList<>();
		map = DPofInstance(i);
		Set set = map.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			OWLDataProperty dp = (OWLDataProperty) it.next();
			String s = Concept(dp);
			arraylist.add(s);
		}
		return arraylist;
	}
	public static ArrayList<String> DPValues(OWLIndividual i){

		HashMap map  = new HashMap<>();
		ArrayList<String> arraylist = new ArrayList<>();
		map = DPofInstance(i);
		Collection collection = map.values();
		Iterator it = collection.iterator();
		while(it.hasNext()){
			String s = it.next().toString();
	    	s = s.substring(s.indexOf("\"")+1,s.lastIndexOf("\""));
			arraylist.add(s);
		}
		return arraylist;
	}
	//对象属性
	public static ArrayList<String> OPKeys(OWLIndividual i){

		HashMap map  = new HashMap<>();
		ArrayList<String> arraylist = new ArrayList<>();
		map = OPofInstance(i);
		Set set = map.keySet();
		Iterator it = set.iterator();
		while(it.hasNext()){
			OWLObjectProperty op = (OWLObjectProperty) it.next();
			String s = Concept(op);
			arraylist.add(s);
		}
		return arraylist;
	}
	public static ArrayList<String> OPValues(OWLIndividual i){

		HashMap map  = new HashMap<>();
		ArrayList<String> arraylist = new ArrayList<>();
		map = OPofInstance(i);
		Collection collection = map.values();
		Iterator it = collection.iterator();
		while(it.hasNext()){
			String s = it.next().toString();
	    	s = s.substring(s.lastIndexOf("#")+1,s.lastIndexOf(">"));
			arraylist.add(s);
		}
		return arraylist;
	}
	public static ArrayList<String> DPName(){
		Set<OWLDataProperty> dp = AllDataProperty();
		return DataProperty(dp);
	}
	public static ArrayList<String> OPName(){
		Set<OWLObjectProperty> op = AllObjectProperty();
		return ObjectProperty(op);
	}
	public static ArrayList<String> DPDomain(OWLDataProperty dp){
		Set<OWLClassExpression> domain = DatatPropertyDomain(dp);
		return Concept(domain);
	}
	public static ArrayList<String> OPDomain(OWLObjectProperty op){
		Set<OWLClassExpression> domain = ObjectPropertyDomain(op);
		return Concept(domain);
	}
	public static ArrayList<String> OPRange(OWLObjectProperty op){
		Set<OWLClassExpression> range = ObjectPropertyRange(op);
		return Concept(range);
	}
}

















