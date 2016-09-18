package demo.jaxrs.server;

import java.util.HashMap;
import java.util.Map;

public class AddressService implements IAddressService {
	private Map<Long,Address> addresses = new HashMap<Long,Address>();
	
	public AddressService(){
		Address a = new Address();
		a.setId(1000L);
		a.setAddress("深圳南山");
		a.setZipCode("10086");
		
		addresses.put(a.getId(), a);
	}

	@Override
	public Address getAddress(String id) {
		System.out.println("getAddress()");
		Address result = addresses.get(Long.parseLong(id));
		return result;
	}

}
