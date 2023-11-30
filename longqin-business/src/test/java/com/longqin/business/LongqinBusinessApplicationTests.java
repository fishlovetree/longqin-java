package com.longqin.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormData;
import com.longqin.business.service.IDesFormService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LongqinBusinessApplicationTests {

	@Autowired
	IDesFormService desFormService;
	
	@Test
	public void contextLoads() {
		try{
			/*DesFormData formData = new DesFormData();
			formData.setTableName("des_shenpi_1_1");
			formData.setColumns("approvalStatus,details,creator,organization_id");
			formData.setVals("1,1,2,1");
		    desFormService.insertFormData(formData);*/
			List<String> columns = new ArrayList<String>();
			columns.add("approvalStatus");
			columns.add("status");
			List<String> vaList = new ArrayList<>();
			vaList.add("2");
			vaList.add("3");
			desFormService.updateFormData("des_shenpi_1_1", columns, vaList, 3);
		}
		catch(Exception e){
			System.out.print(e);
		}
	}
}
