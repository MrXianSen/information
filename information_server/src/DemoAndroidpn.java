import java.util.List;

import org.androidpn.server.model.Information;
import org.androidpn.server.service.InformationService;
import org.androidpn.server.service.ServiceLocator;

//
//  DemoAndroidpn.java
//  FeOA
//
//  Created by LuTH on 2012-3-26.
//  Copyright 2012 flyrise. All rights reserved.
//

public class DemoAndroidpn {

	public static void main(String[] args) {

		InformationService i = ServiceLocator.getInformationService() ;
		
		List<Information> lsit = i.getInformations() ;
		
		for(Information info : lsit)
		{
			System.out.println(info.getID()+" "+info.getTitle()+" "+info.getDate());
		}

	}
}
