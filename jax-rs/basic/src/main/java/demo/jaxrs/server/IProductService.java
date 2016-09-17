package demo.jaxrs.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/customerservice/")
@Produces("text/xml")
public interface IProductService {
	@GET
	@Path("/products/{id}/")
	Product getProduct(@PathParam("id") String id);
}
