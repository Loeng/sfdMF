package com.ekfans.base.user;

import java.util.ArrayList;

public class ArrayRemove {
	public static void main(String[] args) {
		ArrayList<String>  a = new ArrayList<String> (); 
		for(int i=0;i<5;i++){
			a.add("ss"+i);
			System.out.println(a.get(i));
		}
		a.remove(a.get(1));
		System.out.println("********************************");
		for(int i=0;i<4;i++){
			System.out.println(a.get(i));
		}
	}
}
