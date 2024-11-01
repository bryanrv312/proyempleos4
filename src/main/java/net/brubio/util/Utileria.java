package net.brubio.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.multipart.MultipartFile;

public class Utileria {

	private static Cloudinary cloudinary;

	// Inicializar Cloudinary con la URL de conexión
	static {
		Dotenv dotenv = Dotenv.load();
		String cloudinaryUrl = dotenv.get("CLOUDINARY_URL");

		// Verifica si se carga la variable de entorno
		System.out.println("CLOUDINARY_URL: " + cloudinaryUrl); // Línea de prueba

		if (cloudinaryUrl != null) {
			cloudinary = new Cloudinary(cloudinaryUrl);
		} else {
			System.out.println("La variable CLOUDINARY_URL no está definida en el archivo .env");
		}
	}

	public static String guardarArchivo2(MultipartFile multiPart) {
		try {
			Map<String, Object> uploadResult = cloudinary.uploader().upload(multiPart.getBytes(),
					//ObjectUtils.asMap("folder", "empleos/img-vacantes/"));  // Ajusta la carpeta según tus necesidades
					ObjectUtils.emptyMap());
			return uploadResult.get("url").toString(); // Retorna la URL de la imagen en Cloudinary

		} catch (IOException e) {
			System.out.println("Error al subir la imagen: " + e.getMessage());
			return null;
		}
	}

	/*********************************************  upload local storage ***********************************************************/

	public static String guardarArchivo(MultipartFile multiPart, String ruta) {
		// Obtenemos el nombre original del archivo.
		String nombreOriginal = multiPart.getOriginalFilename();
		nombreOriginal.replace(" ", "-");//remplazar los espacios por guion
		String nombreFinal = randomAlphaNumeric(8) + nombreOriginal;
		try {
			// Formamos el nombre del archivo para guardarlo en el disco duro.
			File imageFile = new File(ruta + nombreFinal);
			System.out.println("Archivo: " + imageFile.getAbsolutePath());
			// Guardamos fisicamente el archivo en HD.
			multiPart.transferTo(imageFile);
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}
	
	
	/*
		Metodo para generar una cadena aleatoria de longitud N
		(Agregar caracteres aleatorios al nombre del archivo para evitar duplicados)
	*/
	public static String randomAlphaNumeric(int count) {
		String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length()); 
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}
}