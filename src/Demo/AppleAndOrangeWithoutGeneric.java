package Demo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


class Apple{
	private static long counter;
	private final long id = counter++;
	public long id(){
		return id;
	}
}

class Orange{
	public String get(String s){
		return s;
	}
}
public class AppleAndOrangeWithoutGeneric {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String args[]){
		Set set = new HashSet<>();
		System.out.println(set.isEmpty());
		ArrayList list = new ArrayList();
		for(int i = 0; i < 3; i++)
			list.add(new Apple());
		list.add(new Orange());
		for(int i = 0; i < list.size()-1; i++)
			System.out.println(((Apple) list.get(i)).id());
		
		//��������public����
		String s = ((Orange) list.get(3)).get("hello");
		System.out.println(s);
		System.out.println("ok");
		
		//����Arraylist
		ArrayList list1 = new ArrayList();
		list1 = (ArrayList) list.clone();
		
		for(int i = 0; i < list.size(); i++)
			System.out.println(( list.get(i)));
		for(int i = 0; i < list1.size(); i++)
			System.out.println(( list1.get(i)));
		
		//�������
		list.clear();
		for(int i = 0; i < list.size(); i++)
			System.out.println(( list.get(i)));
		
		//Ԥ���巺��
		ArrayList<Apple> list2 = new ArrayList<Apple>();
		for(int i = 0; i < 3; i++)
			list2.add(new Apple());
		//Ӧ��foreach�﷨
		for(Apple a : list2)
			System.out.println(a);
		
		System.out.println("ok");
		//ʹ�õ�����
		Iterator iterator = list2.iterator();
		Apple a = (Apple) iterator.next();
		System.out.println(a);
		while(iterator.hasNext()){
			System.out.println("ok");
			Apple b = (Apple) iterator.next();
			System.out.println(b);
		}
	}
}
