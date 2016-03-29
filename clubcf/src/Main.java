import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.formula.functions.T;

public class Main {

	static int limit = 7;
	
	public static void main(String[] args) throws Exception {
		//readExcelAndAddInToDB();
		DBConnection connection = new DBConnection();
		List<ClubCF> dbList = connection.getDBRecords(limit);
		float alpha = (float) 0.5;
		String outPut = "";
		
		float matrix[][] = new float[limit][limit];
		
		for (int i = 0; i < dbList.size(); i++) {
			ClubCF t1 = (ClubCF) dbList.get(i);
			List<String> list1 = new ArrayList<String>(Arrays.asList(t1
					.getStemWord().split(",")));
			List<String> list3 = new ArrayList<String>(Arrays.asList(t1
					.getApis().split(",")));
			
			for (int j = 0; j < dbList.size(); j++) {
				outPut += "'";
				ClubCF t2 = (ClubCF) dbList.get(j);
				List<String> list2 = new ArrayList<String>(Arrays.asList(t2
						.getStemWord().split(",")));
				List<String> list4 = new ArrayList<String>(Arrays.asList(t2
						.getApis().split(",")));

				float D_sim = new Main().intersection(list1, list2)
						/ new Main().union(list1, list2);
				float F_sim = new Main().intersection(list3, list4)
						/ new Main().union(list3, list4);

				float C_Sim = (float) (alpha * D_sim + (1 - alpha) * F_sim);

				String output = String.format("%.3f", C_Sim);

				//System.out.print(" " + output + " ");
				
				matrix[i][j] = C_Sim;
				
				outPut += output + "'";
				if ((j + 1) < dbList.size()) {
					outPut += ",";
				}
			}
			// connection.insertInAverageDB(outPut);
			//System.out.print("\n");
		}
		
		PrintMatrix(matrix, 7);
	}
	
	public static void PrintMatrix(float step1[][], float limit) {
		float maxInColumn = 0;
		int row = 0;
		int column = 0;
		for(int i = 0; i < limit; i++){			
			for(int j = 0; j < limit; j++) {
				if(j == 0) 
				System.out.print("C" + (i + 1));
				float value = step1[i][j];
				if(i == j) {
					value = 0;
				}
				if(maxInColumn < value) {
					maxInColumn = value;
					row = (i + 1);
					column = (j + 1);
				}
				System.out.print("\t" + ((i == j) ? "/" : String.format("%.3f", value)));
			}
			System.out.print("\n");
		}
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row + "," + column);
		System.out.print("\n\n\n");
		
		Step2Matrix(step1, row, column, 6);
	}
	
	public static void Step2Matrix(float step1[][], int row,int column, int limit) {
		float maxInColumn = 0;
		int row1 = 0;
		int column1 = 0;
		float step2[][] = new float[limit][limit];
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(row == step2row) {
					step2[step2row][step2Column] = (step1[step2row][step2Column] + step1[step2Column][column]) / 2;
				} else {
					step2[step2row][step2Column] = step1[step2row][step2Column];
				}
			}
		}
		
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(step2Column == 0) {
					if(step2row == (row -1)) {
						System.out.print("[C" + row + ", C" + column + "]");
					} else {
						if(step2row != row || step2Column != column)
							System.out.print("\tC" + (step2row + 1));
					}
				}
				float value = step2[step2row][step2Column];
				
				if(maxInColumn < value) {
					maxInColumn = value;
					row1 = (step2row + 1);
					column1 = (step2Column + 1);
				}
				
				System.out.print("\t" + ((step2row == step2Column) ? "/" : String.format("%.3f", value)));
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row1 + "," + column1);
		
		System.out.print("\n\n\n");
		
		Step3Matrix(step2, row1, column1, 5);
	}
	
	public static void Step3Matrix(float step2[][], int row,int column, int limit) {
		float maxInColumn = 0;
		float step3[][] = new float[limit][limit];
		int row1 = 0;
		int column1 = 0;
		for(int step2row = 0; step2row < limit; step2row++){
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(row == step2row) {
					step3[step2row][step2Column] = (step2[row][column] + step2[column][row]) / 2;
				} else {
					step3[step2row][step2Column] = step2[step2row][step2Column];
				}
			}
		}
		
		for(int step3row = 0; step3row < limit; step3row++){			
			for(int step3Column = 0; step3Column < limit; step3Column++) {
				if(step3Column == 0) {
					if(step3row == (row -1)) {
						System.out.print("[S" + row + ", S" + column + "]");
					} else {
						if(step3row != row || step3Column != column)
							System.out.print("S" + (step3row + 1));
					}
				}
				float value = step2[step3row][step3Column];
				
				if(maxInColumn < value) {
					maxInColumn = value;
					row1 = (step3row + 1);
					column1 = (step3Column + 1);
				}
				
				System.out.print("\t" + ((step3row == step3Column) ? "/" : String.format("%.3f", value)));
				
				System.out.print("\t" + String.format("%.3f", value));
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row1 + "," + column1);
		
		System.out.print("\n\n\n");
		Step4Matrix(step2, row1, column1, 4);
	}
	
	public static void Step4Matrix(float step1[][], int row,int column, int limit) {
		float maxInColumn = 0;
		int row1 = 0;
		int column1 = 0;
		float step2[][] = new float[limit][limit];
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(row == step2row) {
					step2[step2row][step2Column] = (step1[step2row][step2Column] + step1[step2Column][column]) / 2;
				} else {
					step2[step2row][step2Column] = step1[step2row][step2Column];
				}
			}
		}
		
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(step2Column == 0) {
					if(step2row == (row -1)) {
						System.out.print("[C" + row + ", C" + column + "]");
					} else {
						if(step2row != row || step2Column != column)
							System.out.print("\tC" + (step2row + 1));
					}
				}
				float value = step2[step2row][step2Column];
				
				if(maxInColumn < value) {
					maxInColumn = value;
					row1 = (step2row + 1);
					column1 = (step2Column + 1);
				}
				
				System.out.print("\t" + ((step2row == step2Column) ? "/" : String.format("%.3f", value)));
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row1 + "," + column1);
		
		System.out.print("\n\n\n");
		
		Step5Matrix(step2, row1, column1, 3);
	}
	
	public static void Step5Matrix(float step1[][], int row,int column, int limit) {
		float maxInColumn = 0;
		int row1 = 0;
		int column1 = 0;
		float step2[][] = new float[limit][limit];
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(row == step2row) {
					step2[step2row][step2Column] = (step1[step2row][step2Column] + step1[step2Column][column]) / 2;
				} else {
					step2[step2row][step2Column] = step1[step2row][step2Column];
				}
			}
		}
		
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(step2Column == 0) {
					if(step2row == (row -1)) {
						System.out.print("[C" + row + ", C" + column + "]");
					} else {
						if(step2row != row || step2Column != column)
							System.out.print("\tC" + (step2row + 1));
					}
				}
				float value = step2[step2row][step2Column];
				
				if(maxInColumn < value) {
					maxInColumn = value;
					row1 = (step2row + 1);
					column1 = (step2Column + 1);
				}
				
				System.out.print("\t" + ((step2row == step2Column) ? "/" : String.format("%.3f", value)));
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row1 + "," + column1);
		
		System.out.print("\n\n\n");
		
		Step6Matrix(step2, row1, column1, 2);
	}
	
	public static void Step6Matrix(float step1[][], int row,int column, int limit) {
		float maxInColumn = 0;
		int row1 = 0;
		int column1 = 0;
		float step2[][] = new float[limit][limit];
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(row == step2row) {
					step2[step2row][step2Column] = (step1[step2row][step2Column] + step1[step2Column][column]) / 2;
				} else {
					step2[step2row][step2Column] = step1[step2row][step2Column];
				}
			}
		}
		
		for(int step2row = 0; step2row < limit; step2row++){			
			for(int step2Column = 0; step2Column < limit; step2Column++) {
				if(step2Column == 0) {
					if(step2row == (row -1)) {
						System.out.print("[C" + row + ", C" + column + "]");
					} else {
						if(step2row != row || step2Column != column)
							System.out.print("\tC" + (step2row + 1));
					}
				}
				float value = step2[step2row][step2Column];
				
				if(maxInColumn < value) {
					maxInColumn = value;
					row1 = (step2row + 1);
					column1 = (step2Column + 1);
				}
				
				System.out.print("\t" + ((step2row == step2Column) ? "/" : String.format("%.3f", value)));
			}
			System.out.print("\n");
		}
		
		System.out.print("\n\t #Max: " + maxInColumn + "\t #Location: " + row1 + "," + column1);
		
		System.out.print("\n\n\n");
	}

	public float union(List list1, List list2) {	
		Set set = new HashSet();

		set.addAll(list1);
		set.addAll(list2);

		return set.size();
	}

	public float intersection(List list1, List list2) {
		List list = new ArrayList();

		for (String t : (ArrayList<String>) list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}

		return list.size();
	}

	public static void readExcelAndAddInToDB() throws Exception {
		ReadExcelData readExcelData = new ReadExcelData();
		List list = readExcelData.getListFromExcel();
		Iterator iterator = list.iterator();
		int counter = 0;
		while (iterator.hasNext()) {
			ClubCF clubCF = (ClubCF) iterator.next();
			if (counter == 0) {
				counter++;
				continue;
			}
			try {
				stemmer(clubCF);
			} catch (Exception e) {
				e.printStackTrace();
			}
			counter++;
		}
	}

	public static String stemmer(ClubCF clubCF) throws Exception {
		String stemword = "";
		String[] strings = clubCF.getTags().split(",");
		for (int i = 0; i < strings.length; i++) {
			if (i != 0) {
				stemword += ",";
			}
			String word = strings[i];
			char[] w = new char[501];
			Stemmer s = new Stemmer();
			String u;
			char[] array = word.toCharArray();
			for (char ch : array) {
				ch = Character.toLowerCase((char) ch);
				s.add(ch);
			}
			s.stem();
			stemword += s.toString();
		}
		DBConnection connection = new DBConnection();
		connection.insertInDB(clubCF, stemword);
		return stemword;
	}
}
