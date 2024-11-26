package net.brubio.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

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



	public static String guardarArchivo3(MultipartFile multiPart) {
		try {
			byte[] fileBytes = multiPart.getBytes();
			if (fileBytes == null || fileBytes.length == 0) {
				throw new IOException("Error al leer el archivo: el archivo está vacío.");
			}
			// Solo especifica "resource_type" como "raw" para permitir archivos de cualquier tipo
			Map<String, Object> options = ObjectUtils.asMap(
					"resource_type", "raw" // Cambia el tipo de recurso a "raw" sin carpeta
			);
			Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes, options);
			if (uploadResult == null || !uploadResult.containsKey("url")) {
				throw new IOException("Error al subir el archivo: no se recibió una URL válida.");
			}
			return uploadResult.get("url").toString(); // Retorna la URL del archivo en Cloudinary

		} catch (IOException e) {
			System.out.println("Error al subir el archivo: " + e.getMessage());
			return null;
		}
	}


	public static String guardarArchivo2(MultipartFile multiPart) {
		try {
			Map<String, Object> uploadResult = cloudinary.uploader().upload(multiPart.getBytes(),
					//ObjectUtils.asMap("folder", "empleos/img-vacantes/"));  // Ajusta la carpeta en la nube
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

	/********************************  CONVERTIR PDF A IMG  **************************************************************/

	public static String subirPdfComoImagenes(MultipartFile pdfFile) {
		try {
			List<byte[]> imagenes = convertirPdfAImagenes(pdfFile);
			StringBuilder urlsImagenes = new StringBuilder();

			for (byte[] imagen : imagenes) {
				Map<String, Object> uploadResult = cloudinary.uploader().upload(imagen, ObjectUtils.emptyMap());
				String urlImagen = uploadResult.get("url").toString();
				System.err.println("Imagen subida: " + urlImagen);
				urlsImagenes.append(urlImagen).append(","); // Añadimos la URL con una coma como separador
			}
			// Elimina la última coma si hay imágenes subidas
			if (urlsImagenes.length() > 0) {
				urlsImagenes.setLength(urlsImagenes.length() - 1); // Elimina la última coma
			}
			return urlsImagenes.toString(); // Retorna todas las URLs concatenadas


		} catch (IOException e) {
			System.err.println("Error al convertir o subir el PDF: " + e.getMessage());
			return null;
		}
    }

	public static List<byte[]> convertirPdfAImagenes(MultipartFile pdfFile) throws IOException {
		List<byte[]> imagenes = new ArrayList<>();

		try (PDDocument documento = PDDocument.load(pdfFile.getInputStream())) {
			PDFRenderer renderizador = new PDFRenderer(documento);

			for (int i = 0; i < documento.getNumberOfPages(); i++) {
				BufferedImage imagen = renderizador.renderImageWithDPI(i, 300); // Calidad de 300 DPI

				// Convierte cada página en imagen a bytes
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(imagen, "png", baos);
				baos.flush();
				imagenes.add(baos.toByteArray());
				baos.close();
			}
		}
		return imagenes;
	}
	/*********************************************************************************************************************/
}