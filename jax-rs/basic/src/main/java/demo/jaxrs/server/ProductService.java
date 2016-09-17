package demo.jaxrs.server;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.PathParam;

//@Path("/customerservice/")
//@Produces("text/xml")
public class ProductService implements IProductService{
	private Map<Long,Product> products = new HashMap<Long,Product>();
	public ProductService(){
		Product p = new Product();
		p.setId(100L);
		p.setDescription("Product for test");
		
		products.put(p.getId(), p);
	}
//	@GET
//	@Path("/products/{id}/")
	public Product getProduct(@PathParam("id") String id){
		System.out.println("-- === getProduct === --");
		
		return products.get(Long.parseLong(id));
	}
}
