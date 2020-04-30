package Sellar.utils;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import Sellar.Sellar.Sellar;

public abstract class Metodos {

	private static String readAll(Reader rd) throws IOException {

		StringBuilder sb = new StringBuilder();

		int cp;

		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}

		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) throws IOException {

		InputStream is = new URL(url).openStream();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

		String jsonText = readAll(rd);
		is.close();

		return new JSONObject(jsonText);

	}

	public static JSONObject apiImagenes(String parametros) throws IOException {
		JSONObject json = readJsonFromUrl("https://apiperiquito.herokuapp.com/recibo-json.php?imagenes=" + parametros);
		return json;
	}

	public static String saberSeparador(String os) {
		if (os.equals("Linux")) {
			return "/";
		} else {
			return "\\";
		}
	}

	public static void crearScript(String archivo, String contenido, boolean opcional, String os) throws IOException {

		Process aplicacion = null;

		if (os.equals("Linux")) {
			aplicacion = Runtime.getRuntime().exec("bash " + contenido);
			aplicacion.destroy();
		}

		else {

			String iniciar = "";

			if (opcional) {
				iniciar = "start";
			}

			FileWriter flS = new FileWriter(archivo);

			BufferedWriter fS = new BufferedWriter(flS);

			try {
				Runtime aplicacion2 = Runtime.getRuntime();
				fS.write("@echo off");
				fS.newLine();
				fS.write(contenido);
				fS.newLine();
				fS.write("exit");
				aplicacion2 = Runtime.getRuntime();

				try {
					aplicacion2.exec("cmd.exe /K " + iniciar + " " + System.getProperty("user.dir") + "\\" + archivo);
				}

				catch (Exception e) {
//
				}

			}

			finally {
				fS.close();
				flS.close();

			}
		}

	}

	public static void cerrarNavegador() {

		try {

			if (!Sellar.getOs().equals("Linux")) {
				crearScript("cerrar.bat", "taskkill /F /IM chromedriver.exe /T", true, Sellar.getOs());

			}

			else {
				crearScript("cerrar.sh", "kilall chrome", true, Sellar.getOs());
			}

		} catch (Exception e) {
			mensaje("Error al cerrar el navegador", 1);
		}

	}

	public static void cerrarChrome(WebDriver chrome) {

		chrome.close();

		cerrarNavegador();

	}

	public static String extraerNombreArchivo(String extension) throws IOException {

		JSONObject json = apiImagenes("archivo." + extension);

		JSONArray imagenesBD = json.getJSONArray("imagenes_bd");

		String outputFilePath = Sellar.getDirectorioActual() + imagenesBD.get(0).toString();

		return outputFilePath;

	}

	public static int encontrarCadena(LinkedList<String> preguntas, String cadena) {

		int contador = 0;

		int posicion = 0;

		do {

			posicion = cadena.indexOf(preguntas.get(contador));

			if (posicion == -1) {

				++contador;
			}

		}

		while (cadena.indexOf(preguntas.get(contador)) == -1);

		return contador;

	}

	public static String numeroALetras(int num) {

		int numNegativo = num;

		if (num < 0) {
			num *= -1;
		}

		n2t numero = new n2t(num);

		String resultado = numero.convertirLetras(num);

		if (numNegativo < 0) {
			resultado = "menos " + resultado;
		}

		return resultado;

	}

	public static String eliminarEspacios(String cadena) {

		cadena = cadena.trim();

		cadena = cadena.replace("  ", " ");

		cadena = cadena.trim();

		return cadena;
	}

	public static void mensaje(String mensaje, int titulo) {

		String tituloSuperior = "", sonido = "";

		int tipo = 0;

		switch (titulo) {

		case 1:
			tipo = JOptionPane.ERROR_MESSAGE;
			tituloSuperior = "Error";
			sonido = "duck-quack.wav";
			break;

		case 2:
			tipo = JOptionPane.INFORMATION_MESSAGE;
			tituloSuperior = "Informacion";
			sonido = "gong.wav";
			break;

		case 3:
			tipo = JOptionPane.WARNING_MESSAGE;
			tituloSuperior = "Advertencia";
			sonido = "advertencia.wav";
			break;

		default:
			break;

		}

		JLabel alerta = new JLabel(mensaje);

		alerta.setFont(new Font("Arial", Font.BOLD, 18));

		JOptionPane.showMessageDialog(null, alerta, tituloSuperior, tipo);

	}

}