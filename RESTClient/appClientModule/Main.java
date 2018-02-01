
public class Main {
	public static void main(String[] args) {

		System.out.println("Hola cara cola");
		RestClient rClient = new RestClient("http://localhost:8080/prueba");
		rClient.postRequest("<xml></xml>");
		
	}

	/* (non-Java-doc)
	 * @see java.lang.Object#Object()
	 */
	public Main() {
		super();
	}

}