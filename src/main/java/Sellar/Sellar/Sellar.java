package Sellar.Sellar;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import Sellar.utils.Metodos;

@SuppressWarnings("all")

public class Sellar extends javax.swing.JFrame implements ActionListener, ChangeListener {

	private JTextField dni;

	private JPasswordField pass;

	static String separador;

	public static String getSeparador() {
		return separador;
	}

	static String os = System.getProperty("os.name");

	static String directorioActual;

	public static String getDirectorioActual() {
		return directorioActual;
	}

	public static String getOs() {
		return os;
	}

	public Sellar() throws IOException {

		setTitle("Sellar");
		initComponents();

		os = System.getProperty("os.name");

		separador = Metodos.saberSeparador(os);

		initComponents();

		directorioActual = new File(".").getCanonicalPath() + separador;

		this.setVisible(true);

	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		setResizable(false);

		dni = new JTextField();
		dni.setFont(new Font("Tahoma", Font.PLAIN, 14));
		dni.setColumns(10);

		pass = new JPasswordField();
		pass.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNewLabel = new JLabel("DNI");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setIcon(new ImageIcon(Sellar.class.getResource("/imagenes/dni.png")));

		JLabel lblContrasea = new JLabel("Contraseña");
		lblContrasea.setIcon(new ImageIcon(Sellar.class.getResource("/imagenes/key.png")));
		lblContrasea.setFont(new Font("Tahoma", Font.BOLD, 16));

		JButton btnNewButton = new JButton("");

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {

					String dnisellar = Metodos.eliminarEspacios(dni.getText());

					String password = Metodos.eliminarEspacios(pass.getText());

					String pregunta;

					String respuesta = "";

					String busqueda;

					String pregunta2;

					LinkedList<String> numeros = new <String>LinkedList();

					int numero1;

					int numero2;

					LinkedList<String> preguntas = new LinkedList<String>();

					if (!dnisellar.isEmpty() && !password.isEmpty()) {

						ponerPeguntas(preguntas);

						ponerNumeros(numeros);

						boolean encuentraRespuesta = false;

						WebDriver chrome = new ChromeDriver();

						chrome.get(
								"https://ws054.juntadeandalucia.es/autenticacion/login?service=https%3A%2F%2Fws054.juntadeandalucia.es%2Fapdweb%2Fweb%2Fguest%2Fhome");

						chrome.findElement(By.id("username")).sendKeys(dnisellar);

						chrome.findElement(By.id("password")).sendKeys(password);

						pregunta = chrome.findElement(By.className("pregunta")).getText();

						pregunta = pregunta.replace(
								"Como método alternativo para el control de acceso, responde a la siguiente pregunta:",
								"");

						int i = Metodos.encontrarCadena(preguntas, pregunta);

						if (i < preguntas.size()) {

							if (pregunta.indexOf(preguntas.get(i)) != -1) {

								switch (i) {

								case 0:

									boolean suma = false;

									int posicion = pregunta.indexOf(" mas");

									int sumaPosicion = 4;

									if (posicion != -1) {
										suma = true;
									}

									else {

										posicion = pregunta.indexOf(" más");

										if (posicion != -1) {
											suma = true;
										}

										else {

											posicion = pregunta.indexOf(" menos");

											sumaPosicion = 6;

										}
									}

									busqueda = pregunta.substring(pregunta.indexOf("resultado de ") + 13, posicion);

									numero1 = numeros.indexOf(busqueda);

									busqueda = pregunta.substring(posicion + sumaPosicion, pregunta.indexOf("?"));

									numero2 = numeros.indexOf(busqueda);

									if (suma) {
										respuesta = Metodos.numeroALetras(numero1 + numero2);
									}

									else {
										respuesta = Metodos.numeroALetras(numero1 - numero2);
									}

									encuentraRespuesta = true;

									break;

								case 1:

									respuesta = "tres";
									encuentraRespuesta = true;
									break;

								case 2:

									busqueda = pregunta.substring(pregunta.indexOf("mitad de ") + 9,
											pregunta.indexOf("?"));

									respuesta = Metodos.numeroALetras(numeros.indexOf(busqueda) / 2);

									encuentraRespuesta = true;
									break;

								case 3:

									busqueda = pregunta.substring(pregunta.indexOf("tiene una ") + 10,
											pregunta.indexOf(" en"));

									pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1, pregunta.indexOf("]"));

									String[] letras = pregunta2.split(",");

									boolean comprobacion = false;

									int x = 0;

									busqueda = busqueda.toLowerCase();

									while (!comprobacion) {

										if (letras[x].indexOf(busqueda) != -1) {

											respuesta = letras[x];

											comprobacion = true;
										}

										x++;
									}

									encuentraRespuesta = true;

									break;

								case 4:

									LinkedList<String> meses = new <String>LinkedList();

									meses.add("enero");
									meses.add("febrero");
									meses.add("marzo");
									meses.add("abril");
									meses.add("mayo");
									meses.add("junio");
									meses.add("julio");
									meses.add("agosto");
									meses.add("septiembre");
									meses.add("octubre");
									meses.add("noviembre");
									meses.add("diciembre");

									pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1, pregunta.indexOf("]"));

									String[] preguntaMeses = pregunta2.split(",");

									LinkedList<Integer> mesesBusqueda = new <Integer>LinkedList();

									for (int y = 0; y < preguntaMeses.length; y++) {

										mesesBusqueda.add(meses.indexOf(preguntaMeses[y]));

									}

									int menor;

									menor = mesesBusqueda.get(0);

									for (int z = 0; z < mesesBusqueda.size(); z++) {

										if (mesesBusqueda.get(z) < menor) {
											menor = mesesBusqueda.get(z);
										}

									}

									respuesta = meses.get(menor);

									encuentraRespuesta = true;

									break;

								case 5:
									encuentraRespuesta = true;
									respuesta = "sesenta";
									break;

								case 6:

									encuentraRespuesta = true;
									respuesta = "izquierda";
									break;

								case 7:

									busqueda = pregunta.substring(pregunta.indexOf("suman ") + 6,
											pregunta.indexOf(" y"));

									numero1 = numeros.indexOf(busqueda);

									busqueda = pregunta.substring(pregunta.indexOf("y") + 1, pregunta.indexOf("?"));

									numero2 = numeros.indexOf(busqueda);

									respuesta = Metodos.numeroALetras(numero1 + numero2);

									encuentraRespuesta = true;

									break;

								case 8:

									respuesta = "Madrid";

									encuentraRespuesta = true;

									break;

								case 9:

									if (pregunta.indexOf("y acaba") == -1) {
										busqueda = pregunta.substring(pregunta.indexOf("por ") + 4,
												pregunta.indexOf(" en"));

										pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1,
												pregunta.indexOf("]"));

										String[] cadenas = pregunta2.split(",");

										busqueda = busqueda.toLowerCase();

										for (int xy = 0; xy < cadenas.length; xy++) {

											if (cadenas[xy].startsWith(busqueda)) {

												respuesta = cadenas[xy];

												xy = cadenas.length;
											}

										}
									}

									else {

										String termina = "";

										busqueda = pregunta.substring(pregunta.indexOf("por ") + 4,
												pregunta.indexOf(" y"));

										termina = pregunta.substring(pregunta.indexOf("acaba en ") + 9,
												pregunta.indexOf(" en esta"));

										pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1,
												pregunta.indexOf("]"));

										String[] cadenas = pregunta2.split(",");

										busqueda = busqueda.toLowerCase();

										termina = termina.toLowerCase();

										for (int xy = 0; xy < cadenas.length; xy++) {

											if (cadenas[xy].startsWith(busqueda) && cadenas[xy].endsWith(termina)) {

												respuesta = cadenas[xy];

												xy = cadenas.length;
											}

										}

									}

									encuentraRespuesta = true;

									break;

								case 10:
									respuesta = "leche";
									encuentraRespuesta = true;
									break;

								case 11:
									respuesta = "diciembre";
									encuentraRespuesta = true;
									break;

								case 12:
									respuesta = "vaso";
									encuentraRespuesta = true;
									break;

								case 13:
									respuesta = "sesenta";
									encuentraRespuesta = true;
									break;

								case 14:
									respuesta = "blanca";
									encuentraRespuesta = true;
									break;

								case 15:
									respuesta = "silla";
									encuentraRespuesta = true;
									break;
								case 16:
									respuesta = "armario";
									encuentraRespuesta = true;
									break;

								case 17:
									respuesta = "elefante";
									encuentraRespuesta = true;
									break;

								case 18:
									respuesta = "tigre";
									encuentraRespuesta = true;
									break;

								case 19:

									busqueda = pregunta.substring(pregunta.indexOf("termina en ") + 11,
											pregunta.indexOf(" en esta"));

									pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1, pregunta.indexOf("]"));

									String[] cadenas = pregunta2.split(",");

									busqueda = busqueda.toLowerCase();

									for (int xy = 0; xy < cadenas.length; xy++) {

										if (cadenas[xy].endsWith(busqueda)) {

											respuesta = cadenas[xy];

											xy = cadenas.length;
										}

									}
									encuentraRespuesta = true;
									break;

								case 20:
									busqueda = pregunta.substring(pregunta.indexOf("acaba en ") + 9,
											pregunta.indexOf(" en esta"));

									pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1, pregunta.indexOf("]"));

									String[] cadenas2 = pregunta2.split(",");

									busqueda = busqueda.toLowerCase();

									for (int xy = 0; xy < cadenas2.length; xy++) {

										if (cadenas2[xy].endsWith(busqueda)) {

											respuesta = cadenas2[xy];

											xy = cadenas2.length;
										}

									}

									encuentraRespuesta = true;
									break;

								case 21:

									int numeroletras = 0;

									busqueda = pregunta.substring(pregunta.indexOf("tiene ") + 6,
											pregunta.indexOf(" letras"));

									numeroletras = Integer.parseInt(busqueda);

									pregunta2 = pregunta.substring(pregunta.indexOf("[") + 1, pregunta.indexOf("]"));

									String[] cadenas3 = pregunta2.split(",");

									busqueda = busqueda.toLowerCase();

									for (int xy = 0; xy < cadenas3.length; xy++) {

										if (cadenas3[xy].length() == numeroletras) {

											respuesta = cadenas3[xy];

											xy = cadenas3.length;
										}

									}

									encuentraRespuesta = true;
									break;

								case 22:
									respuesta = "murcielago";
									encuentraRespuesta = true;
									break;

								case 23:
									respuesta = "lapiz";
									encuentraRespuesta = true;
									break;

								case 24:
									respuesta = "coche";
									encuentraRespuesta = true;
									break;

								default:

									break;

								}

							}

							i++;

						}

						else {

							Metodos.mensaje("Error, por favor, vuelva a pulsar el Play", 1);

							encuentraRespuesta = true;
						}

						chrome.findElement(By.id("respCognitiva")).sendKeys(respuesta);

						chrome.findElement(By.className("btn-submit")).click();

						chrome.get("https://ws054.juntadeandalucia.es/apdweb/group/10210/2");
						chrome.get("https://ws054.juntadeandalucia.es/apdweb/group/10210/2");

						if (chrome.getCurrentUrl().equals(
								"https://ws054.juntadeandalucia.es/autenticacion/login?service=https%3A%2F%2Fws054.juntadeandalucia.es%2Fapdweb%2Fweb%2Fguest%2Fhome")) {

							Metodos.cerrarChrome(chrome);

							Metodos.mensaje("Por favor, inténtelo otra vez", 3);
						}

						else {

							JavascriptExecutor js = (JavascriptExecutor) chrome;

							js.executeScript("submitForm(document._Renovacion_fm);");

							js.executeScript("document.body.style.zoom = 1.0;");

							js.executeScript("scroll(0, 400);");

							File src = ((TakesScreenshot) chrome).getScreenshotAs(OutputType.FILE);

							FileUtils.copyFile(src, new File(Metodos.extraerNombreArchivo("pdf")));

							chrome.get(
									"https://ws054.juntadeandalucia.es/apdweb/group/10210/2?p_p_id=Renovacion&p_p_lifecycle=1&p_p_state=exclusive&p_p_mode=view&p_p_col_id=column-2&p_p_col_count=1&_Renovacion_struts_action=%2Fext%2Frenovacion%2FemitirDarde");

							int resp = JOptionPane.showConfirmDialog(null, "¿Ha terminado la descarga?");

							if (resp == 0) {

								Metodos.cerrarChrome(chrome);
							}
						}
					}

					else {
						Metodos.mensaje("Por favor, introduce un dni y una contraseña", 2);
					}

				}

				catch (Exception e1) {
					e1.printStackTrace();
				}

			}

			private void ponerPeguntas(LinkedList<String> preguntas) {

				preguntas.add("Cuál es el resultado de ");

				preguntas.add("Cuántos lados tiene un triángulo");

				preguntas.add("es la mitad de ");

				preguntas.add("palabra tiene una");

				preguntas.add("es el primer mes del año en esta lista?");

				preguntas.add("¿Cuántos segundos tiene un minuto?");

				preguntas.add("¿Con cuál mano escribe un zurdo?");

				preguntas.add("Cuánto suman ");

				preguntas.add("es la capital de España?");

				preguntas.add("palabra empieza por ");

				preguntas.add("se puede beber de esta lista?");

				preguntas.add("es el último mes del año?");

				preguntas.add("que se usa para contener agua");

				preguntas.add("segundos tiene un minuto?");

				preguntas.add("color es la leche?");
				preguntas.add("no tiene teclas en los objetos de esta lista?");
				preguntas.add("no es un animal en esta lista?");
				preguntas.add("no es un color?");
				preguntas.add("es un animal en esta lista?");
				preguntas.add("palabra termina en ");
				preguntas.add("palabra acaba en ");
				preguntas.add("letras en esta lista?");
				preguntas.add("palabras tiene cinco vocales?");
				preguntas.add("se usa para escribir");
				preguntas.add("tiene cuatro ruedas");
			}

			private void ponerNumeros(LinkedList<String> numeros) {
				numeros.add("cero");
				numeros.add("uno");
				numeros.add("dos");
				numeros.add("tres");
				numeros.add("cuatro");
				numeros.add("cinco");
				numeros.add("seis");
				numeros.add("siete");
				numeros.add("ocho");
				numeros.add("nueve");
				numeros.add("diez");
				numeros.add("once");
				numeros.add("doce");
				numeros.add("trece");
				numeros.add("catorce");
				numeros.add("quince");
				numeros.add("dieciséis");
				numeros.add("diecisiete");
				numeros.add("dieciocho");
				numeros.add("diecinueve");
				numeros.add("veinte");
				numeros.add("veintiuno");
				numeros.add("veintidos");
				numeros.add("veintitres");
				numeros.add("veinticuatro");
				numeros.add("veinticinco");
				numeros.add("veintiseis");
				numeros.add("veintisiete");
				numeros.add("veintiocho");
				numeros.add("veintinueve");
				numeros.add("treinta");
			}

		});

		btnNewButton.setIcon(new ImageIcon(Sellar.class.getResource("/imagenes/start.png")));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(dni, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblContrasea)
										.addGap(12).addComponent(pass, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
								.addGroup(layout.createSequentialGroup().addGap(197).addComponent(btnNewButton,
										GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel)
								.addComponent(dni, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
								.addComponent(pass, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblContrasea, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(29, Short.MAX_VALUE)));
		getContentPane().setLayout(layout);
		setSize(new Dimension(499, 203));
		setLocationRelativeTo(null);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
