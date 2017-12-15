/* 
 * Lo scopo di questo programma è generare turni di lavoro din una zienda specifica, avente 2 turni con ogni turno 2 linee di lavoro
 * il programma deve tenere conto che tutti i lavoratori (tranne per scelta personale) dovranno alternare i turni tra mattina e sera in base
 * alla tariffa extra notturna guadagnata nel tempo, in modo che tutti i dipendenti abbiano guadagnato piu o meno le sesse ore notturne in modo totalmente automatico.
 * il programma inoltre salvera i turni su un foglio di calcolo. Se i turni sul foglio di calcolo vengono cambiati. allora il programma riconoscerà il cambiamento
 * e aggiungerà o sottrarrà la tariffa notturna lavorata e non.
 */
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;



class TurniLavoroNew {
	
	public static void main(String[] args)
		throws IOException { //Sicurezza per eventuali errori di Input e Output
				String path = "options.proprieties";
				if (Preferenze.FileExist(path) == false) {
					Preferenze.InitFile();
				}
				ArrayList <Dipendente> dipendenteArrayList = new ArrayList<Dipendente>();
				int Nacht = 0;
				int NumMitarbeiterLinee = 1;
				int minNacht = Integer.parseInt(Preferenze.getOnePreference("NACHT_MIN_MITARBEITER"));
				int NumMitarbeiterNacht = Integer.parseInt(Preferenze.getOnePreference("NUM_MITARBEITER_LINEE1_NACHT"));
				//IMPUT KalenderWoche
				LocalDate date = LocalDate.of(2017, 1, 1);
				int inputWeek = 0;
				String nomeFile = "";
				int yn;
				IOFile save = new IOFile();
				do { //CREAZIONE DIPENDENTE PROVVISORIA
					Scanner scan = new Scanner (System.in);
					System.out.print("Volete Aggiungere un nuovo Dipendente? 1 = Y , 2 = N" + "\n");
					yn = scan.nextInt();
					if (yn == 1) {
						String var;
						Dipendente dipendente  = new Dipendente();
						scan.nextLine();
						System.out.println("Personalnummer: " + "\n");
						long personalnummer = (long)scan.nextInt();
						dipendente.setPersonalnummer(personalnummer);
						scan.nextLine();
						System.out.println("Nome: " + "\n");
						var = scan.nextLine();
						dipendente.setNome(var);
						System.out.println("Cognome: " + "\n");
						var = scan.nextLine();
						dipendente.setCognome(var);
						System.out.println("capacita linee: es. 1, 2 oppure digitare 3 per entrambe" + "\n");
						int Capacitalinea = scan.nextInt();
						dipendente.setLineaLavoro(Capacitalinea);
						System.out.println("Linee Leiter? es. 0 = N, 1, 2 oppure digitare 3 per entrambe" + "\n");
						int linieLeiter = scan.nextInt();
						dipendente.setLinieLeiter(linieLeiter);
						System.out.println(dipendente.getCognome() + " Vuole lavorare solo la mattina? 1 = Y, 2 = N" + "\n");
						int soloMattina = scan.nextInt();
						if (soloMattina == 1) {
						dipendente.setSoloMattina(true);
						}
						System.out.println(dipendente.getCognome() + " quale Giorno Libero? DDMMYY per giorno libero, 1 per NO");
						long free = scan.nextLong();
						if (free != 1) {
							int DD = (int) (free/10000);
							int MM = (int) (free - (DD*10000));
							MM = MM /100;
							int YY = (int) (free - (DD*10000) - (MM*100) + (2000));
							LocalDate freeDay = LocalDate.of(YY, MM, DD);
							save.freeday(dipendente, freeDay, "frei_als_wunch");
						}
						System.out.println("Dipendente Salvato!" + "\n");
						scan.nextLine();
						nomeFile = (dipendente.getCognome() + ".dbs");
						save.ExportObjectToFile("database", nomeFile, dipendente);
					}
				} while (yn == 1);
				
				//Carico i Dipendenti dal Database
				dipendenteArrayList = save.ImportObjectFromFile("database");
				Scanner scan = new Scanner (System.in);
				System.out.println("volete aggiungere un giorno libero ad un dipendente? 1 si, 2 no");
				int z = scan.nextInt();
				if (z == 1) {
					System.out.println("inserire cognome dipendente");
					String surname = scan.nextLine();
					surname = scan.nextLine();
						Dipendente dipendente;
						for (int x=0; x < dipendenteArrayList.size(); x++) {
							dipendente = dipendenteArrayList.get(x);
							String cognome = dipendente.getCognome().toLowerCase();
							System.out.println(dipendente.getCognome());
							if (cognome.compareTo(surname.toLowerCase()) == 0) {
								System.out.println(dipendente.getCognome() + " quale Giorno Libero? DDMMYY per giorno libero, 1 per NO");
								long free = scan.nextLong();
								if (free != 1) {
									int DD = (int) (free/10000);
									int MM = (int) (free - (DD*10000));
									MM = MM /100;
									int YY = (int) (free - (DD*10000) - (MM*100) + (2000));
									LocalDate freeDay = LocalDate.of(YY, MM, DD);
									save.freeday(dipendente, freeDay, "frei_als_wunch");
								}
							} else {
								System.out.println("cognome non trovato");
							}
						}
				}
				
				
				do {
					//Scanner scan = new Scanner (System.in);
					System.out.print("Welche kalenderWoche wollen Sie?" + "\n");
					inputWeek = scan.nextInt();
					// guardare se la KW e´gia presente 
					String directory = "turni_" + date.getYear();
					boolean weekAlredyExist = save.checkWeek(inputWeek, directory);
					if (weekAlredyExist == true) {
						System.out.println("This week Alredy Exist");
						inputWeek = 100;
					} else {
						if (inputWeek < 1 || inputWeek > 53) {
							System.out.print("week´s number is not correct" + "\n");
						}
					}
				} while (inputWeek < 1 || inputWeek > 53);
				inputWeek -= 1;
				date = date.plusWeeks(inputWeek);
				ArrayList <String> turniWeek = new ArrayList<String>();
				ArrayList <String> turnoDipendente = new ArrayList<String>();
				
				//SCELTA DI CHI LAVORA LA MATTINA O LA SERA
					for (int x = 0 ; x < dipendenteArrayList.size(); x++) {
					Dipendente dipendente = dipendenteArrayList.get(x);
					if (dipendente.getSoloMattina() == true) {
						dipendente.setTagNacht(1); //TAG
					} else {
						if (Nacht <= minNacht) {
							Nacht++;
							dipendente.setTagNacht(2); //Nacht
							if (NumMitarbeiterLinee <= NumMitarbeiterNacht) {
								NumMitarbeiterLinee++;
							} else {
							}
						} else {
							dipendente.setTagNacht(1); //TAG
						}
					}
					int giornoLibero = 0;
					ArrayList <LocalDate> giornoLiberoDT;
						if (dipendente.getTagNacht() == 2) { 
							giornoLibero = new  Random() .nextInt(5) ; // 0 per DOM, 1 per LUN, 2 per MAR, 3 ... VEN non si è mai liberi.
							giornoLiberoDT = save.checkFreeDay(dipendente, "frei_als_wunch");
							for (int y = 0; y < giornoLiberoDT.size(); y++) {
									if (giornoLiberoDT.get(y).isAfter(date) && giornoLiberoDT.get(y).isBefore(date.plusDays(7)) || giornoLiberoDT.get(y).isEqual(date)) {
										DayOfWeek DoW = DayOfWeek.from(giornoLiberoDT.get(y));
										if (DoW == DayOfWeek.SUNDAY) {
											giornoLibero = 0;
										} else if (DoW == DayOfWeek.MONDAY) {
											giornoLibero = 1;
										} else if (DoW == DayOfWeek.TUESDAY) {
											giornoLibero = 2;
										} else if (DoW == DayOfWeek.WEDNESDAY) {
											giornoLibero = 3;
										} else if (DoW == DayOfWeek.THURSDAY) {
											giornoLibero = 4;
										} else if (DoW == DayOfWeek.WEDNESDAY) {
											giornoLibero = 5;
										}
									}
								}	
						}
					dipendente.setGiornoLibero(giornoLibero);
				}
				
				//GENERAZIONE TURNI E SALVATAGGIO	
				String directory = "turni_" + date.getYear();
				save.InitTurni(directory,  String.valueOf(inputWeek+1) + ".txt", date);
				for (int x = 0; x < dipendenteArrayList.size(); x++) {
					Dipendente dipendente = dipendenteArrayList.get(x);
					turnoDipendente = SwitchTurni.generaTurni(dipendente.getGiornoLibero(), dipendente.getTagNacht(), dipendente, dipendente.malattia, date.with(DayOfWeek.SUNDAY));
					turnoDipendente.add("\n");
					turniWeek.add(dipendente.getCognome() +  " " + turnoDipendente);
					dipendente.setWeekShift(turnoDipendente);
					System.out.println("\n");
					nomeFile = (dipendente.getCognome() + ".txt");
					directory = "mitarbeiter";
					save.ExportShift(directory, nomeFile, dipendente, date);
					directory = "turni_" + date.getYear();
					save.ExportTurniDiTutti(directory, String.valueOf(inputWeek+1) + ".txt", turniWeek.get(x));
					nomeFile = (dipendente.getCognome() + ".txt");
					save.ExportShift("mitarbeiter", nomeFile, dipendente, date);
					//List<String> saved = save.ImportFile(directory, nomeFile);
					nomeFile = (dipendente.getCognome() + ".dbs");
					save.ExportObjectToFile("database", nomeFile, dipendente);
				}
				
				turniWeek.forEach(System.out::println);
				dipendenteArrayList = getMenoOre.OrdinePerNottiArrayList(dipendenteArrayList);
				
				for(int h = 0; h < dipendenteArrayList.size(); h++) {
					Dipendente dipendenteVar = dipendenteArrayList.get(h);
					System.out.println(dipendenteVar.getCognome() + " TOT: " + dipendenteVar.getTotZuSchlag());
				}
				directory = "database";
				dipendenteArrayList = save.ImportObjectFromFile(directory); //.forEach(System.out::println);
				//Dipendente dipendente = dipendenteArrayList.get(1);
				//System.out.println(dipendente.getNome() + dipendente.getCognome());
				//Inserire inserimento da Tastiera su Database
				//Chiedere se qualcuno è malato, le OreNotturne, OreFestivita, OreDomeniche le riceverà comunque 
				//Implementare metodo per quanto riguarda: Licenziamenti, azubi o lifeFirma che se ne vanno.
				//Inserire Matrice con turni di tutti i Dipendenti, poi salvarla su database
				
				}

}