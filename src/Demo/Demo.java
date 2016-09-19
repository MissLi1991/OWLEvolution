package Demo;

import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import com.hp.hpl.jena.graph.Factory;
import com.hp.hpl.jena.vocabulary.OWL;

public class Demo {


	public static void main(String args[]){
		OWLParse ontology = new OWLParse();
		String string = "animal";
		OWLClass animal = OWLParse.GetOWLClass(string);
		String string1 = "Bird";
		OWLClass bird = OWLParse.GetOWLClass(string1);
		String string3 = "WarmBloodVertebrate";
		OWLClass WarmBloodVertebrate = OWLParse.GetOWLClass(string3);
		String string4 = "Invertebrate";
		OWLClass Invertebrate = OWLParse.GetOWLClass(string4);
		String string6 = "Concept";
		String string5 = "hello";
		OWLClass hello = OWLParse.GetOWLClass(string5);
		OWLClass concept = OWLParse.GetOWLClass(string6);
		System.out.println(bird.getSignature());
		System.out.println(animal.getSignature());
		
		/*
		 * 测试
		 * 1.owlparse.GetChildren 
		 * 2.OWLParse.GetParents
		 */
		Set<OWLClassExpression> subclass = OWLParse.GetChildren(animal);
		Set<OWLClassExpression> superclass = OWLParse.GetParents(bird);
		System.out.println("animal的子类：");
		System.out.println(subclass);
		System.out.println("bird的父类：");
		System.out.println(superclass);
		
		/*
		 * 测试
		 * 3.owlparse.FindAncestor 
		 */
		Set<OWLClassExpression> ancestor = OWLParse.FindAncestor(bird);
		System.out.println("bird的祖先：");
		System.out.println(ancestor);
		//Set<OWLClassExpression> ancestor1 = OWLParse.FindAncestor(fish);
		//System.out.println("fish的祖先：");
		//System.out.println(ancestor1);
		
		//ChangeParentOf.ChangeParent(WarmBloodVertebrate,concept, animal);
		
		/*
		 * 测试
		 * 4.owlparse.RemoveParentAxiom 
		 */
		//OWLParse.RemoveParentAxiom(bird, concept);
		//OWLParse.AddParentAxiom( WarmBloodVertebrate,concept);
		/*
		 * 测试
		 * 5.OWLEvolution.AddParent
		 */
		//AddParent.setClass(Invertebrate,animal);
		//AddParent.JudgeAncestor();
		//AddParent.setClass(bird, Invertebrate);
		//AddParent.JudgeAncestor();
		//AddParent.setClass(bird, WarmBloodVertebrate);
		//AddParent.JudgeAncestor();
		/*
		 * 测试
		 * 6.OWLEvolution.ChangeParentOf
		 */
		//ChangeParentOf.ChangeParent(hello, bird, WarmBloodVertebrate);
		
		/*
		 * 测试
		 * 7.OWLParse.RemoveConcept
		 * OWLAPI中会自动遍历与之相关的概念、属性和实例全部删除
		 */
		//AddParent.setClass(hello, bird);
		//AddParent.JudgeAncestor();
		//OWLParse.RemoveParentAxiom(bird,Invertebrate);
		//OWLParse.RemoveConcept(bird);
		//OWLParse.RemoveConcept(Invertebrate);
	
		/*
		 * 测试
		 *9.OWLEvolution.RemoveConcept.JudgeSubconceptOrphaned
		 */
		
		System.out.println("ok");
		//System.out.println(RemoveConcept.JudgeSubconceptOrphaned(Invertebrate));
		//System.out.println(RemoveConcept.JudgeSubconceptOrphaned(WarmBloodVertebrate));
		//System.out.println(RemoveConcept.JudgeSubconceptOrphaned(bird));
		//RemoveConcept.Remove(bird);
		//RemoveConcept.Remove(Invertebrate);
		
		//RemoveParentOf.RemoveParent(hello, bird);
		
		String string8 = "bird";
		OWLIndividual instance =  OWLParse.GetIndividual(string8);
		String string9 = "WarmBloodVertebrate";
		OWLIndividual instance1 =  OWLParse.GetIndividual(string9);
	
		//OWLParse.AddInstance(instance, bird);
		//OWLParse.AddInstance(instance1, WarmBloodVertebrate);
		String string10 = "eat";
		OWLObjectProperty op = OWLParse.ObjectProperty(string10);
		String string11 = "name";
		String string12 = "Lily";
		String string13= "eating";
		String string14= "Eat";
		OWLObjectProperty op1 = OWLParse.ObjectProperty(string13);
		OWLObjectProperty op2 = OWLParse.ObjectProperty(string14);
		OWLDataRange r = OWLParse.StringDataType();

		System.out.println(OWLParse.IndividualOfClass(bird));
		//System.out.println(OWLParse.ObjectPropertyDomain(op1));
		//System.out.println(OWLParse.ObjectPropertyRange(op1));
		//SetDomain.SetObjectPropertyDomain(op, bird);
		//SetRange.SetObjectPropertyRange(op, WarmBloodVertebrate);
		OWLDataProperty dp = OWLParse.DataProperty(string11);
		//SetDomain.SetDataPropertyDomain(dp, animal);
		/*AddObjectProperty.AddOP(op, Invertebrate, bird);
		AddObjectProperty.AddOP(op, instance, instance1);
		AddObjectProperty.AddOP(op1, instance1, instance);
		AddDataProperty.AddOP(dp, WarmBloodVertebrate, r);
		AddDataProperty.AddDP(dp, instance, "m");*/
		//AddObjectProperty.AddOP(op, bird, WarmBloodVertebrate);
		//AddDataProperty.AddOP(dp, WarmBloodVertebrate, r);
		//OWLParse.SetOPDomain(op, bird);
		//OWLParse.SetOPRange(op, WarmBloodVertebrate);
		/*OWLParse.SetOPDomain(op1, animal);
		OWLParse.SetOPRange(op1, Invertebrate);*/
		//ChangeParentOfObjectProperty.ChangeParentofOP(op1, op, op2);
		//AddParentOfObjectProperty.setClass(op1, op2);
		//AddParentOfObjectProperty.JudgeAncestor();
		//OWLParse.AddParentOfObjectProperty(op1, op);
		//OWLParse.AddParentOfObjectProperty(op, op2);
		//OWLParse.SetDPDomain(dp, bird);
		//OWLParse.SetDPRange(dp, r);
		//OWLParse.AddDataProperty(dp, instance, string12);
		//OWLParse.AddObjectProperty(op, instance, instance1);
		System.out.println(OWLParse.AllObjectProperty());

		//OWLParse.RemoveInstance(instance, bird);
		//OWLParse.RemoveInstance(instance,WarmBloodVertebrate);
		//OWLParse.RemoveIndividual(instance);
		//ChangeInstanceOf.AddInstance(instance, bird, WarmBloodVertebrate);
		System.out.println(OWLParse.ObjectPropertyDomain(op));
		System.out.println(OWLParse.ObjectPropertyRange(op));
		System.out.println(OWLParse.DatatPropertyDomain(dp));
		System.out.println(OWLParse.DatatPropertyRange(dp));
		System.out.println(OWLParse.ClassOfIndividual(instance));
		//OWLParse.RemoveObjectProperty(op);
		//打印所有类
		//System.out.println(OWLParse.AllClass());
		Iterator<OWLClassExpression> it = OWLParse.AllClass().iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
		
		Set<OWLClassExpression> superclass1 = OWLParse.GetParents(bird);
		System.out.println(superclass1);
		
		System.out.println("bird: " + OWLParse.Concept(bird));
		System.out.println("bird's parent: " + OWLParse.Parent(bird));
		System.out.println("animal's child: " + OWLParse.Child(animal));
		System.out.println("animal's sibling: "+ OWLParse.Sibling(animal));
		System.out.println("bird's individual: "+ OWLParse.Individual(bird));
		System.out.println(""+ OWLParse.DPofInstance(instance));
		System.out.println(OWLParse.DPKeys(instance));
		System.out.println(OWLParse.DPValues(instance));
		System.out.println(""+ OWLParse.OPofInstance(instance));
		System.out.println(OWLParse.OPKeys(instance));
		System.out.println(OWLParse.OPValues(instance));
		System.out.println(OWLParse.OPName());
		System.out.println(OWLParse.DPName());
		System.out.println(OWLParse.DPDomain(dp));
		System.out.println(OWLParse.OPDomain(op1));
		System.out.println(OWLParse.OPRange(op1));
		/*
		 * 测试
		 * 8.OWLParse.save
		 */
		OWLParse.Save();
		
	}
}
