package Demo;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;




/*
 * AddParent(c,c0).add axiom c0 is the parent of c
 * 	A cycle will be induced when c0 used to be an ancestor of c,Recursive call ChangeParentOf()
 */
class AddParent{
	private static int impact = 2;
	private static OWLClass subclass = null;
	private static OWLClass parentclass = null;
	private static OWLClass newparent;
	
	//获取影响值
	public int getImpact(){
		return impact;
	}
	
	//为执行操作的概念赋值
	public static void setClass(OWLClass child,OWLClass parent){
		subclass = child;
		parentclass = parent;
	}
	
	//判断新加父节点是否子节点的祖先
	public static void JudgeAncestor(){
		//得到祖先
		Set<OWLClassExpression> ancestor;//到底要不要顺序
		ancestor = OWLParse.FindAncestor(subclass);
		//判断是否为祖先

		if(ancestor.contains(parentclass)){
			Set<OWLClassExpression> parent = OWLParse.GetParents(parentclass);
			//使用迭代器中的第一个元素作为新的父类
			Iterator<OWLClassExpression> iterator = parent.iterator();
			newparent = (OWLClass) iterator.next();
			//迭代对每个子类进行ChangeParentOf
			Set<OWLClassExpression> subClass = OWLParse.GetChildren(parentclass);
			Iterator<OWLClassExpression> iterator1 = subClass.iterator();
			while(iterator1.hasNext()){
				OWLClass child = (OWLClass) iterator1.next();
				ChangeParentOf.ChangeParent(child, parentclass, newparent);
			}
			OWLParse.AddParentAxiom(subclass, parentclass);
		}else{
			OWLParse.AddParentAxiom(subclass, parentclass);
		}
	}

}
/*
 * ChangeParentOf(c,c0,c1),change parent of c from c0 to c1
 * A cycle will be induced when c0 used to be an ancestor of c,Recursive call ChangeParentOf()
 */
class ChangeParentOf{
	private static OWLClass child = null;
	private static OWLClass oldparent = null;
	private static OWLClass newparent = null;
	private static final int impact = 2;
	
	//获取影响值
	public static int getImpact(){
		return impact;
	}
	

	//为执行操作的概念赋值
	/*public static void setClass(OWLClass subclass,OWLClass oldparentclass,OWLClass newparentclass){
		child = subclass;
		oldparent = oldparentclass;
		newparent = newparentclass;
	}*/
	public static void ChangeParent(OWLClass child2, OWLClass parentclass,
			OWLClass newparent2){
		child = child2;
		oldparent = parentclass;
		newparent = newparent2;
		
		OWLParse.RemoveParentAxiom(child, oldparent);
		//得到祖先
		Set<OWLClassExpression> ancestor;//到底要不要顺序
		ancestor = OWLParse.FindAncestor(child);
	
		//判断是否为祖先
		if(ancestor.contains(newparent)){
			Set<OWLClassExpression> parent = OWLParse.GetParents(oldparent);
			//使用迭代器中的第一个元素作为新的父类
			Iterator<OWLClassExpression> iterator = parent.iterator();
			OWLClass newparent1 = (OWLClass) iterator.next();
			//迭代对每个子类进行ChangeParentOf
			Set<OWLClassExpression> subClass = OWLParse.GetChildren(oldparent);
			Iterator<OWLClassExpression> iterator1 = subClass.iterator();
			while(iterator1.hasNext()){
				OWLClass child = (OWLClass) iterator1.next();
				ChangeParentOf.ChangeParent(child, oldparent, newparent1);
				
			}
			OWLParse.AddParentAxiom(child, newparent);
		}else{
			OWLParse.AddParentAxiom(child, newparent);		
			}
	}
}
/*
 * RemoveParent(c).Remove class c 
 * 	Human decide:1.Remove subclass 2.only remove class and change parent of subclass from c to c'parent 
 */
class  RemoveConcept{
		private static OWLClass subclass;
		private static OWLClass superclass;
		private static final int impact = 4;
		
		public int getImpact(){
			return impact;
		}
		public static Set<OWLClassExpression> JudgeSubconceptOrphaned(OWLClass parent){
			superclass = parent;
			Set<OWLClassExpression> child = OWLParse.GetChildren(superclass);
			Iterator<OWLClassExpression> it = child.iterator();
			Set<OWLClassExpression> set = new HashSet<OWLClassExpression>();
			Set<OWLClassExpression> parentclass;
			while(it.hasNext()){
				subclass = (OWLClass) it.next();
				parentclass = OWLParse.GetParents(subclass);
				if(parentclass.size() == 1){
					set.add(subclass);
				}
			}
			return set;
		}
		public static Set<OWLIndividual> JudgeIndividualOrphaned(OWLClass parent){
		
			Set<OWLIndividual> individual = OWLParse.IndividualOfClass(parent);
			Iterator<OWLIndividual> it = individual.iterator();
			Set<OWLIndividual> set = new HashSet<OWLIndividual>();
			Set<OWLClassExpression> parentclass;
			while(it.hasNext()){
				OWLIndividual instance = (OWLIndividual) it.next();
				parentclass = OWLParse.ClassOfIndividual(instance);
				if(parentclass.size() == 1){
					set.add(instance);
				}
			}
			return set;
		}
		public static void RemoveIndividual(OWLClass parent){
			
			Set<OWLClassExpression> supset = OWLParse.GetParents(parent);
			Iterator<OWLClassExpression> iterator = supset.iterator();
			OWLClass sup = (OWLClass) iterator.next();
			
			Set<OWLIndividual> subinstance = JudgeIndividualOrphaned(parent);
			if(!subinstance.isEmpty()){
				Iterator<OWLIndividual> it = subinstance.iterator();
				while(it.hasNext()){
					OWLIndividual i = (OWLIndividual) it.next();
					//1.修改父类
					ChangeInstanceOf.AddInstance(i, parent, sup);
					//2.全部删除
					RemoveInstance.RemoveInstance(i);
				}
			}
			OWLParse.RemoveConcept(parent);
		}
		public static void Remove(OWLClass parent){
			
			Set<OWLClassExpression> supset = OWLParse.GetParents(parent);
			Iterator<OWLClassExpression> iterator = supset.iterator();
			OWLClass sup = (OWLClass) iterator.next();
			
			Set<OWLClassExpression> child = JudgeSubconceptOrphaned(parent);
			if(!child.isEmpty()){
				Iterator<OWLClassExpression> it = child.iterator();
				while(it.hasNext()){
					OWLClass sub = (OWLClass) it.next();
					//1.修改父类
					//ChangeParentOf.ChangeParent(sub, parent, sup);
					//2.全部删除
					Remove(sub);
				}
			}
			OWLParse.RemoveConcept(parent);
		}
	
	}
/*
 * RemoveParent(c,c0).Remove parent axiom 
 * 	 
 */
class RemoveParentOf{
	private static OWLClass subclass;
	private static OWLClass superclass;
	public static void RemoveParent(OWLClass child,OWLClass parent){
		subclass = child;
		superclass = parent;
		OWLParse.RemoveParentAxiom(subclass, superclass);
	}
}
/*
 **************************实例********************************************
 */
class RemoveInstance{
	private static OWLIndividual instance;

	
	public static void RemoveInstance(OWLIndividual i){
		instance = i;

		OWLParse.RemoveIndividual(instance);
	}
}
class SetInstanceOf{
	private static OWLIndividual instance;
	private static OWLClass concept;
	
	public static void SetInstance(OWLIndividual i, OWLClass c){
		instance = i;
		concept = c;
		OWLParse.AddInstance(instance, concept);
	}
}
/*
 * ChangeInstance(i,c0,c1).
 */
class ChangeInstanceOf{
	private static OWLIndividual instance;
	private static OWLClass oldconcept;
	private static OWLClass newconcept;
	
	public static void AddInstance(OWLIndividual i, OWLClass c1,OWLClass c2){
		instance = i;
		oldconcept = c1;
		newconcept = c2;
		OWLParse.RemoveInstance(instance, oldconcept);
		OWLParse.AddInstance(instance, newconcept);
		
		}
	}
/*
 **************************属性********************************************
 */
class AddObjectProperty{
	private static OWLObjectProperty objectproperty;
	private static OWLClass concept1;
	private static OWLClass concept2;
	private static OWLIndividual individual1;
	private static OWLIndividual individual2;
	
	public static void AddOP(OWLObjectProperty op,OWLClass c1,OWLClass c2){
		objectproperty = op;
		concept1 = c1;
		concept2 = c2;
		
		SetDomain.SetObjectPropertyDomain(objectproperty, concept1);
		SetRange.SetObjectPropertyRange(objectproperty, concept2);
		OWLParse.SetOPDomain(objectproperty, concept1);
		OWLParse.SetOPRange(objectproperty, concept2);
	}
	public static void AddOP(OWLObjectProperty op,OWLIndividual i1,OWLIndividual i2){
		objectproperty = op;
		individual1 = i1;
		individual2 = i2;
		
		OWLParse.AddObjectProperty(objectproperty, individual1,individual2);
	}
}
class AddDataProperty{
	private static OWLDataProperty dataproperty;
	private static OWLClass concept;
	private static OWLDataRange datarange;
	private static OWLIndividual individual1;
	
	public static void AddOP(OWLDataProperty dp,OWLClass c,OWLDataRange r){
		dataproperty = dp;
		concept = c;
		datarange = r;
		
		OWLParse.SetDPDomain(dataproperty, concept);
		OWLParse.SetDPRange(dataproperty, datarange);
	}
	public static void AddDP(OWLDataProperty dp,OWLIndividual i1,String string){
		dataproperty = dp;
		individual1 = i1;

		OWLParse.AddDataProperty(dataproperty, individual1, string);
	}
	public static void AddDP(OWLDataProperty dp,OWLIndividual i1,int i){
		dataproperty = dp;
		individual1 = i1;

		OWLParse.AddDataProperty(dataproperty, individual1, i);
	}
	public static void AddDP(OWLDataProperty dp,OWLIndividual i1,boolean b){
		dataproperty = dp;
		individual1 = i1;

		OWLParse.AddDataProperty(dataproperty, individual1, b);
	}
	public static void AddDP(OWLDataProperty dp,OWLIndividual i1,double d){
		dataproperty = dp;
		individual1 = i1;

		OWLParse.AddDataProperty(dataproperty, individual1, d);
	}
	public static void AddDP(OWLDataProperty dp,OWLIndividual i1,float f){
		dataproperty = dp;
		individual1 = i1;

		OWLParse.AddDataProperty(dataproperty, individual1, f);
	}
	
}
class AddParentOfObjectProperty{
	private static OWLObjectProperty subop;
	private static OWLObjectProperty parentop;
	private static OWLObjectProperty newparent;
	//为执行操作的概念赋值
		public static void setClass(OWLObjectProperty child,OWLObjectProperty parent){
			subop = child;
			parentop = parent;
		}
		
		//判断新加父节点是否子节点的祖先
		public static void JudgeAncestor(){
			//得到祖先
			Set<OWLObjectPropertyExpression> ancestor;//到底要不要顺序
			ancestor = OWLParse.AncestorObjectProperty(subop);
			//判断是否为祖先

			if(ancestor.contains(parentop)){
				Set<OWLObjectPropertyExpression> parent = OWLParse.ParentObjectProperty(parentop);
				//使用迭代器中的第一个元素作为新的父类
				if(parent.isEmpty()){
					newparent = OWLParse.TopObjectProperty();
				}
				else{
				Iterator<OWLObjectPropertyExpression> iterator = parent.iterator();
				newparent = (OWLObjectProperty) iterator.next();
				}
				//迭代对每个子类进行ChangeParentOf
				Set<OWLObjectPropertyExpression> subClass = OWLParse.ChildObjectProperty(parentop);
				Iterator<OWLObjectPropertyExpression> iterator1 = subClass.iterator();
				while(iterator1.hasNext()){
					OWLObjectProperty child = (OWLObjectProperty) iterator1.next();
					ChangeParentOfObjectProperty.ChangeParentofOP(child, parentop, newparent);
				}
				OWLParse.AddParentOfObjectProperty(subop, parentop);
			}else{
				OWLParse.AddParentOfObjectProperty(subop, parentop);
			}
		}
}
class ChangeParentOfObjectProperty{
	private static OWLObjectProperty subop;
	private static OWLObjectProperty oldparentop;
	private static OWLObjectProperty newparentop;
	
	public static void ChangeParentofOP(OWLObjectProperty op, OWLObjectProperty op1,OWLObjectProperty op2){
		subop = op;
		oldparentop = op1;
		newparentop = op2;
		
		OWLParse.RemoveObjectProperty(op, oldparentop);
		//得到祖先
		Set<OWLObjectPropertyExpression> ancestor;//到底要不要顺序
		ancestor = OWLParse.AncestorObjectProperty(op);
	
		//判断是否为祖先
		if(ancestor.contains(newparentop)&&OWLParse.TopObjectProperty()!=newparentop){
			Set<OWLObjectPropertyExpression> parent = OWLParse.ParentObjectProperty(newparentop);
			//使用迭代器中的第一个元素作为新的父类
			Iterator<OWLObjectPropertyExpression> iterator = parent.iterator();
			OWLObjectProperty newparent1 =  (OWLObjectProperty) iterator.next();
			//迭代对每个子类进行ChangeParentOf
			Set<OWLObjectPropertyExpression> sub = OWLParse.ChildObjectProperty(oldparentop);
			Iterator<OWLObjectPropertyExpression> iterator1 = sub.iterator();
			while(iterator1.hasNext()){
				OWLObjectProperty child = (OWLObjectProperty) iterator1.next();
				ChangeParentOfObjectProperty.ChangeParentofOP(child, oldparentop, newparent1);
				
			}
			OWLParse.AddParentOfObjectProperty(subop, newparentop);
		}else{
			OWLParse.AddParentOfObjectProperty(subop, newparentop);		
			}
	}
}	
class AddParentOfDataProperty{
	private static OWLDataProperty subdp;
	private static OWLDataProperty parentdp;
	private static OWLDataProperty newparent;
	//为执行操作的概念赋值
		public static void setClass(OWLDataProperty child,OWLDataProperty parent){
			subdp = child;
			parentdp = parent;
		}
		
		//判断新加父节点是否子节点的祖先
		public static void JudgeAncestor(){
			//得到祖先
			Set<OWLDataPropertyExpression> ancestor;//到底要不要顺序
			ancestor = OWLParse.AncestorDataProperty(subdp);
			//判断是否为祖先

			if(ancestor.contains(parentdp)){
				Set<OWLDataPropertyExpression> parent = OWLParse.ParentDataProperty(parentdp);
				//使用迭代器中的第一个元素作为新的父类
				if(parent.isEmpty()){
					newparent = OWLParse.TopDataProperty();
				}
				else{
				Iterator<OWLDataPropertyExpression> iterator = parent.iterator();
				newparent = (OWLDataProperty) iterator.next();
				}
				//迭代对每个子类进行ChangeParentOf
				Set<OWLDataPropertyExpression> subClass = OWLParse.ChildDataProperty(parentdp);
				Iterator<OWLDataPropertyExpression> iterator1 = subClass.iterator();
				while(iterator1.hasNext()){
					OWLDataProperty child = (OWLDataProperty) iterator1.next();
					ChangeParentOfDataProperty.ChangeParentofDP(child, parentdp, newparent);
				}
				OWLParse.AddParentOfDataProperty(subdp, parentdp);
			}else{
				OWLParse.AddParentOfDataProperty(subdp, parentdp);
			}
		}
}
class ChangeParentOfDataProperty{
	private static OWLDataProperty subop;
	private static OWLDataProperty oldparentdp;
	private static OWLDataProperty newparentdp;
	
	public static void ChangeParentofDP(OWLDataProperty dp, OWLDataProperty dp1,OWLDataProperty dp2){
		subop = dp;
		oldparentdp = dp1;
		newparentdp = dp2;
		
		OWLParse.RemoveDataProperty(dp, oldparentdp);
		//得到祖先
		Set<OWLDataPropertyExpression> ancestor;//到底要不要顺序
		ancestor = OWLParse.AncestorDataProperty(dp);
	
		//判断是否为祖先
		if(ancestor.contains(newparentdp)&&OWLParse.TopDataProperty()!=newparentdp){
			Set<OWLDataPropertyExpression> parent = OWLParse.ParentDataProperty(newparentdp);
			//使用迭代器中的第一个元素作为新的父类
			Iterator<OWLDataPropertyExpression> iterator = parent.iterator();
			OWLDataProperty newparent1 =  (OWLDataProperty) iterator.next();
			//迭代对每个子类进行ChangeParentOf
			Set<OWLDataPropertyExpression> sub = OWLParse.ChildDataProperty(oldparentdp);
			Iterator<OWLDataPropertyExpression> iterator1 = sub.iterator();
			while(iterator1.hasNext()){
				OWLDataProperty child = (OWLDataProperty) iterator1.next();
				ChangeParentOfDataProperty.ChangeParentofDP(child, oldparentdp, newparent1);
				
			}
			OWLParse.AddParentOfDataProperty(subop, newparentdp);
		}else{
			OWLParse.AddParentOfDataProperty(subop, newparentdp);		
			}
	}
}
class SetDomain{
	private static OWLObjectProperty objectproperty;
	private static OWLDataProperty dataproperty;
	

	public static void SetObjectPropertyDomain(OWLObjectProperty op,OWLClass c){
		Set<OWLObjectProperty> allop = OWLParse.AllObjectProperty();
		objectproperty = op;
		Set<OWLClassExpression> domain = OWLParse.ObjectPropertyDomain(objectproperty);
		if(allop.contains(objectproperty)&&!domain.isEmpty()){
		domain.clear();
		Set<OWLObjectPropertyExpression> ancestor = OWLParse.AncestorObjectProperty(objectproperty);
		Iterator<OWLObjectPropertyExpression> it = ancestor.iterator();
		while(it.hasNext()){
			OWLObjectProperty parent = (OWLObjectProperty) it.next();
			Set<OWLClassExpression> d = OWLParse.ObjectPropertyDomain(parent);
			Iterator<OWLClassExpression> it1 = d.iterator();
			while(it1.hasNext()){
				domain.add(it1.next());
			}
			
		}
		System.out.println(domain);
		if(!domain.isEmpty()&&!domain.contains(c)){
			Set<OWLObjectPropertyExpression> parent = OWLParse.ParentObjectProperty(objectproperty);
			Iterator<OWLObjectPropertyExpression> it1  = parent.iterator();
			while(it.hasNext()){
				OWLObjectProperty sup = (OWLObjectProperty) it1.next();
				OWLParse.RemoveObjectProperty(objectproperty,sup);
			}
		}
		OWLParse.SetOPDomain(objectproperty, c);
		}
		OWLParse.SetOPDomain(objectproperty, c);
	}
	public static void SetDataPropertyDomain(OWLDataProperty dp,OWLClass c){
		dataproperty = dp;
		Set<OWLDataProperty> alldp = OWLParse.AllDataProperty();
		Set<OWLClassExpression> domaindp = OWLParse.DatatPropertyDomain(dataproperty);
	
		if(alldp.contains(dataproperty)&& !domaindp.isEmpty()){
		domaindp.clear();
		Set<OWLDataPropertyExpression> ancestor = OWLParse.AncestorDataProperty(dataproperty);
		Iterator<OWLDataPropertyExpression> it = ancestor.iterator();
		while(it.hasNext()){
			OWLDataProperty parent = (OWLDataProperty) it.next();
			Set<OWLClassExpression> d = OWLParse.DatatPropertyDomain(parent);
			domaindp.addAll(d);
		}
		if(!domaindp.contains(c)){
			Set<OWLDataPropertyExpression> parent = OWLParse.ParentDataProperty(dataproperty);
			Iterator<OWLDataPropertyExpression> it1  = parent.iterator();
			while(it.hasNext()){
				OWLDataProperty sup = (OWLDataProperty) it1.next();
				OWLParse.RemoveDataProperty(dataproperty, sup);
			}
		}
		OWLParse.SetDPDomain(dataproperty, c);
		}
		OWLParse.SetDPDomain(dataproperty, c);
	}
}
class SetRange{
	private static OWLObjectProperty objectproperty;
	private static OWLDataProperty dataproperty;
	public static void SetObjectPropertyRange(OWLObjectProperty op,OWLClass c){
		objectproperty = op;
		Set<OWLObjectProperty> allop = OWLParse.AllObjectProperty();
		Set<OWLClassExpression> range = OWLParse.ObjectPropertyRange(objectproperty);
		if(allop.contains(objectproperty)&& !range.isEmpty()){
		range.clear();
		Set<OWLObjectPropertyExpression> ancestor = OWLParse.AncestorObjectProperty(objectproperty);
		Iterator<OWLObjectPropertyExpression> it = ancestor.iterator();
		while(it.hasNext()){
			OWLObjectProperty parent = (OWLObjectProperty) it.next();
			Set<OWLClassExpression> d = OWLParse.ObjectPropertyRange(parent);
			range.addAll(d);
		}
		if(!range.contains(c)||range.isEmpty()){
			Set<OWLObjectPropertyExpression> parent = OWLParse.ParentObjectProperty(objectproperty);
			Iterator<OWLObjectPropertyExpression> it1  = parent.iterator();
			while(it.hasNext()){
				OWLObjectProperty sup = (OWLObjectProperty) it1.next();
				OWLParse.RemoveObjectProperty(objectproperty,sup);
			}
		}
		OWLParse.SetOPRange(objectproperty, c);
		}
		OWLParse.SetOPRange(objectproperty, c);
	}

}
