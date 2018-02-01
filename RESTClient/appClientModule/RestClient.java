

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.ws.rs.core.MediaType;




/**
 * Clase de petición a servicios REST.
 *
 */
public class RestClient
{
//	private final Logger  logger = LoggerFactory.getLogger(this.getClass().getName());

	/** The end point to send information. */
	private final String  url;
	/** The connection timeout. */
	private final Integer connectionTimeOut;
	/** The read timeout. */
	private final Integer readTimeOut;

	/**
	 * Constructor principal. Recibe la url del endpoint a ser consultado.
	 * 
	 * @param url
	 */
	public RestClient(String url)
	{
		this.url = url;
		this.connectionTimeOut = 10000; // tiemeout de conexiÃ³n
		this.readTimeOut = 10000; // timeout para contestar
	}

	/**
	 * This method sends data to Rest server using a POST request.
	 * 
	 * @param sendBodyData
	 *            Data to send.
	 * @param sendHeadersData
	 *            The headers data to send.
	 * @return response from the Rest Server.
	 * @throws Exception
	 * @throws SocketTimeoutException
	 */
	public String postRequest(String sendBodyData) 
	{
		String result = null;
		if (url != null)
		{
			// int resultado = 0;
			HttpURLConnection client = null;
			try
			{
				client = (HttpURLConnection) new URL(url).openConnection();
				client.setDoOutput(true);
				client.setRequestMethod("POST");
				client.setRequestProperty("Content-Type", MediaType.APPLICATION_XML);

				if (this.connectionTimeOut != null)
					client.setConnectTimeout(this.connectionTimeOut);

				if (this.readTimeOut != null)
					client.setReadTimeout(this.readTimeOut);

				OutputStream outputStream = client.getOutputStream();
				outputStream.write(sendBodyData.getBytes());

				outputStream.flush();
				outputStream.close();

				int status = client.getResponseCode(); // recibe contestaciÃ³n
				if (status == HttpURLConnection.HTTP_OK)
				{
					result = readInputStream(client.getInputStream(), null, null);
//					logger.debug("Cadena a recibida: " + result);
					System.out.println("Cadena recibida "+result);
				}
				else
				{
					System.out.println("REST. Error en el código de respuesta REST: "+status);
//					logger.error("REST. Error en el código de respuesta REST: {}, de URL: {}", status, url);
				}
			} catch (SocketTimeoutException e)
			{
				System.out.println("Error esperando la rspuesta");
				//logger.error("Error esperando la respuesta de '{}'", url, e);
			} catch (ProtocolException | MalformedURLException e)
			{
				System.out.println("Error de comunicación con el servidor REST URL: "+url);
//				logger.error("Error de comunicación con el servicio REST. Url: '{}'", url, e);
			} catch (Exception e)
			{
				System.out.println("Error procesando la respuesta ");
//				logger.error("Error procesando la respuesta de '{}'", url, e);
			} finally
			{
				if (client != null)
					client.disconnect();
			}
		}

		return result;
	}

	/**
	 * Lee un inputStream y lo trasnforma a String
	 * 
	 * @param inputStream
	 *            Flujo de entrada de la informaciÃ³n
	 * 
	 * @param result
	 *            String para concatener la lectura
	 * 
	 * @return
	 *         String leido
	 * @throws IOException
	 */
	private String readInputStream(InputStream inputStream, String result, Charset charset) throws IOException
	{
		BufferedReader responseBuffer;
		if (charset == null)
		{
			responseBuffer = new BufferedReader(new InputStreamReader(inputStream));
		}
		else
		{
			responseBuffer = new BufferedReader(new InputStreamReader(inputStream, charset));
		}
		String output = null;
		String aux = result;
		while ((output = responseBuffer.readLine()) != null)
		{
			if (aux == null)
			{
				aux = new String();
			}
			aux = aux.concat(output);
		}

		return aux;
	}

}
