
import java.util.regex.Pattern;


public class Expression {

	private String Expression;
	private char trigonometric_mode= 'd'; // can be (d)eg, (r)ad or (g)rad;

	Expression() {
		this.Expression="";
	}

	Expression(String s) {
		this.Expression= s;
	}

	public void set(String s) {
		this.Expression= s;
	}

	public String get() {
		return this.Expression;
	}

	public String evaluate() {
		try {
			String ret= (this.eval_main(this.Expression)+"");
			return ret;
		}
		catch (NumberFormatException e) {
			System.out.println(e);
			return "InValid String Build...";
		}
		catch(Exception e) {
			return "Fatal Error, Unable to Evaluate...";
		}
	}

	public String evaluate(String expr) {
		try {
			this.Expression= expr;
			String ret= (this.eval_main(expr)+"");
			return ret;
		}
		catch (NumberFormatException e) {
			//System.out.println(e);
			return "InValid String Build...";
		}
		catch(Exception e) {
			return "Fatal Error, Unable to Evaluate...";
		}
	}

	public void setMode(char m) {
		if(m== 'd' || m== 'r' || m=='g')
			this.trigonometric_mode= m;
	}

	public char getMode() {
		return this.trigonometric_mode;
	}

	private double eval_main(String expr) throws Exception {

		expr= this.formatExpression(expr);

		char to_be_added_mul[]= {'(', 's', 'c', 't', 'S', 'C', 'T', 'L'};

		for(int i=1; i<expr.length(); i++) {
			for(int j=0; j<to_be_added_mul.length; j++)
				if(to_be_added_mul[j]== expr.charAt(i)) {
					if(is_a_number(expr.charAt(i-1))) {
						expr= expr.substring(0, i)+ '*' + expr.substring(i);
						i= -1;
						break;
					}
				}
		}

		if(expr.length()==0) {
			return 0.0;
		}
		//Evaluating Braces...
		
		while(true) {
			int no_of_braces= 0;
			for(int i=0; i<expr.length(); i++) //count braces
				if(expr.charAt(i)== '(')
					no_of_braces++;
			if(no_of_braces<1)
				break;  //Break if no Braces left
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)=='(') {
					int sub_braces= 0;
					String sub_expr= "";
					for(int j=i+1; j<expr.length(); j++) {
						if(expr.charAt(j)== '(') {
							sub_braces++;
							sub_expr+= '(';
						}
						else if(expr.charAt(j)!= ')') {
							sub_expr+= expr.charAt(j);
						}
						else if(expr.charAt(j)== ')') {
							if(sub_braces>0) {
								sub_expr+= ')';
								sub_braces--;
							}
							else {
								i= expr.length();
								//System.out.println("Calling "+ sub_expr);
								double sub_res= eval_main(sub_expr);
								sub_expr= '('+sub_expr+')';
								expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
								break;
							}
						}
					}
				}
			}
		} //there Should not be any braces after this...

		// Multiple '+' or '-' evaluation...

		for(int i=0; i<expr.length()-1; i++) {
			if(expr.charAt(i)== '-' && expr.charAt(i+1)== '-') {
				//System.out.print(i+"--   "+expr);
				expr= expr.substring(0, i)+ '+'+ expr.substring(i+2);
				//System.out.println("  ->  "+expr);
				i=-1;
			}
			else if(expr.charAt(i)== '+' && expr.charAt(i+1)== '+') {
				//System.out.print(i+"++   "+expr);
				expr= expr.substring(0, i)+ '+'+ expr.substring(i+2);
				//System.out.println("  ->  "+expr);
				i=-1;
			}
			else if(expr.charAt(i)== '-' && expr.charAt(i+1)== '+') {
				//System.out.print(i+"-+   "+expr);
				expr= expr.substring(0, i)+ '-'+ expr.substring(i+2);
				//System.out.println("  ->  "+expr);
				i=-1;
			}
			else if(expr.charAt(i)== '+' && expr.charAt(i+1)== '-') {
				//System.out.print(i+"+-   "+expr);
				expr= expr.substring(0, i)+ '-'+ expr.substring(i+2);
				//System.out.println("  ->  "+expr);
				i=-1;
			}
		}

		// System.out.println(expr);

		// for nPr and nCr
		// n(b)r means combination, n(p)r means permutation

		String sub_expr_n= "", sub_expr_r= "";
		for(int i=0; i<expr.length(); i++) {
			if(is_a_number(expr.charAt(i))) {
				sub_expr_n= sub_expr_n+expr.charAt(i);
			}
			else if(expr.charAt(i)== 'b') {
				for(int j=i+1; j<expr.length(); j++) {
					if(is_a_number(expr.charAt(j)))
						sub_expr_r= sub_expr_r+ expr.charAt(j);
					else {
						break;
					}
				}
				if(((int) Double.parseDouble(sub_expr_n))< (int) Double.parseDouble(sub_expr_r))
					throw new NumberFormatException();

				int sub_res_= this.find_combination((int) Double.parseDouble(sub_expr_n), (int) Double.parseDouble(sub_expr_r));
				expr= expr.replaceFirst(Pattern.quote(sub_expr_n+'b'+sub_expr_r), ""+sub_res_);
				sub_expr_n= "";
				sub_expr_r= "";
				i=-1;
			}
			else if(expr.charAt(i)== 'p') {
				for(int j=i+1; j<expr.length(); j++) {
					if(is_a_number(expr.charAt(j)))
						sub_expr_r= sub_expr_r+ expr.charAt(j);
					else {
						break;
					}
				}
				if(((int) Double.parseDouble(sub_expr_n))< (int) Double.parseDouble(sub_expr_r))
					throw new NumberFormatException();

				int sub_res_= this.find_permutation((int) Double.parseDouble(sub_expr_n), (int) Double.parseDouble(sub_expr_r));
				expr= expr.replaceFirst(Pattern.quote(sub_expr_n+'p'+sub_expr_r), ""+sub_res_);
				sub_expr_n= "";
				sub_expr_r= "";
				i=-1;

			}
			else {
				sub_expr_n= "";
			}
		}

		expr= this.remove_e(expr);

		//System.out.println(expr);


		// for factorial

		String sub_expr_f= "";
		for(int i=0; i<expr.length(); i++) {
			if(is_a_number(expr.charAt(i))) {
				sub_expr_f= sub_expr_f+ expr.charAt(i);
			}
			else if(expr.charAt(i)== '!') {
				String sub_res= this.fact((int) Double.parseDouble(sub_expr_f)) + "";
				expr= expr.replaceFirst(Pattern.quote(sub_expr_f+'!'), ""+sub_res);
				expr= this.remove_e(expr);
				i= -1;
			}
			else
				sub_expr_f= "";
		}

		

		// for (L)ogarithm...

		for(int i=0; i<expr.length(); i++) {
			if(expr.charAt(i)== 'L') {
				String sub_expr_l= this.get_val_from(expr, i+1);
				if(sub_expr_l.length()<1)
					throw new NumberFormatException();
				double sub_res_l= Math.log(Double.parseDouble(sub_expr_l));
				sub_expr_l= 'L'+ sub_expr_l;
				expr= expr.replaceFirst(Pattern.quote(sub_expr_l), sub_res_l+"");
				expr= this.remove_e(expr);
				i= -1;
			}
		}

		

		// for Square root

		for(int i=0; i<expr.length(); i++) {
			if(expr.charAt(i)== '√') {
				String sub_expr_r_= this.get_val_from(expr, i+1);
				String sub_res_r_= Math.sqrt(Double.parseDouble(sub_expr_r_))+"";
				expr= expr.replaceFirst(Pattern.quote("√" +sub_expr_r_), sub_res_r_+"");
				expr= this.remove_e(expr);
				i= -1;
			}
		}

		

		//add prefix '~' to negative numbers

		char operations[]= {'^', '/', '*', '+', '-'};

		if(expr.charAt(0)=='-')
			expr= expr.replaceFirst(Pattern.quote("-"), "~");
		else if(expr.charAt(0)=='+')
			expr= expr.replaceFirst(Pattern.quote("+"), "");
		for(int i=1; i<expr.length()-2; i++) {
			for(int j=0; j<operations.length; j++) {
				if(operations[j]== expr.charAt(i)) {
					if(expr.charAt(i+1)== '-') {
						expr= (expr.substring(0,i+1)+'~'+expr.substring(i+2));
					}
					else if(expr.charAt(i+1)== '+') {
						expr= (expr.substring(0,i+1)+expr.substring(i+2));
					}
				}
			}
		}

		//System.out.println(expr);

		// Evaluate sin, cos and other Trigonometry op...

		//stands for (s)in, (c)os, (t)an, a(S)in, a(C)os, a(T)an ....

		for(int i=0; i<expr.length(); i++) {
			boolean got_trig= false;
			String sub_expr= "";
			Double sub_res= 0.0;
			if(expr.charAt(i)== 's') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 's'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd')
					sub_res= Math.sin(Double.parseDouble(tmp_sub_oper)*Math.PI/180);
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.sin(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.sin(Double.parseDouble(tmp_sub_oper)*Math.PI/200);
				got_trig= true;
			}
			else if(expr.charAt(i)== 'c') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 'c'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd')
					sub_res= Math.cos(Double.parseDouble(tmp_sub_oper)*Math.PI/180);
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.cos(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.cos(Double.parseDouble(tmp_sub_oper)*Math.PI/200);
				got_trig= true;
			}
			else if(expr.charAt(i)== 't') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 't'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd')
					sub_res= Math.tan(Double.parseDouble(tmp_sub_oper)*Math.PI/180);
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.tan(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.tan(Double.parseDouble(tmp_sub_oper)*Math.PI/200);
				got_trig= true;
			}
			else if(expr.charAt(i)== 'S') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 'S'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd') 
					sub_res= Math.asin(Double.parseDouble(tmp_sub_oper))*180/Math.PI;
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.asin(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.asin(Double.parseDouble(tmp_sub_oper))*200/Math.PI;
				got_trig= true;
			}
			else if(expr.charAt(i)== 'C') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 'C'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd') 
					sub_res= Math.acos(Double.parseDouble(tmp_sub_oper))*180/Math.PI;
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.acos(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.acos(Double.parseDouble(tmp_sub_oper))*200/Math.PI;
				got_trig= true;
			}
			else if(expr.charAt(i)== 'T') {
				String tmp_sub_oper= this.get_val_from(expr, i+1);
				sub_expr= 'T'+ tmp_sub_oper;
				if(tmp_sub_oper.charAt(0)=='~')
					tmp_sub_oper= '-'+ tmp_sub_oper.substring(1);
				if(this.trigonometric_mode== 'd') 
					sub_res= Math.atan(Double.parseDouble(tmp_sub_oper))*180/Math.PI;
				else if(this.trigonometric_mode== 'r')
					sub_res= Math.atan(Double.parseDouble(tmp_sub_oper));
				else if(this.trigonometric_mode== 'g')
					sub_res= Math.atan(Double.parseDouble(tmp_sub_oper))*200/Math.PI;
				got_trig= true;
			}
			if(got_trig) {
				//System.out.println(sub_expr + "  ->  " + sub_res);
				i=-1;
				String sub_res_s= ""+ sub_res;
				if(sub_res_s.charAt(0)== '-')
					sub_res_s= '~'+ sub_res_s.substring(1);
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res_s);


				expr= this.remove_e(expr);
			}
		}

		//System.out.println(expr);

		/*

		char trigonometric_operators[]= {'s', 'c', 't', 'S', 'C', 'T'};



		for(int i=0; i<expr.length(); i++) {
			System.out.println(expr);
			boolean calc_again= false;
			String oper_= "";
			for(int j=0; j<trigonometric_operators.length && !calc_again; j++) {
				if(trigonometric_operators[j]== expr.charAt(i)) {
					boolean oper_found= false;
					oper_= oper_+ expr.charAt(i+1);
					for(int k=i+2; k<expr.length() && !oper_found && !calc_again; k++) {
						for(int l=0; l<trigonometric_operators.length && !calc_again; l++) {
							if(expr.charAt(k)== trigonometric_operators[l])
								oper_found= true;
						}
						for(int l=0; l<operations.length; l++) {
							if(expr.charAt(k)== operations[l])
								oper_found= true;
						}
						if(!oper_found)
							oper_= oper_+expr.charAt(k);
					}

					if(expr.charAt(i)== 's') {
						String sub_expr= 's'+oper_;
						Double sub_res= Math.sin(Double.parseDouble(oper_));
						expr= expr.replaceFirst(Pattern.quote(sub_expr), sub_res+"");
						i=0;
						calc_again= true;
					}
					else if(expr.charAt(i)== 'c') {
						String sub_expr= 'c'+oper_;
						Double sub_res= Math.cos(Double.parseDouble(oper_));
						expr= expr.replaceFirst(Pattern.quote(sub_expr), sub_res+"");
						i=0;
						calc_again= true;
					}
					else if(expr.charAt(i)== 't') {
						String sub_expr= 't'+oper_;
						Double sub_res= Math.tan(Double.parseDouble(oper_));
						expr= expr.replaceFirst(Pattern.quote(sub_expr), sub_res+"");
						i=0;
						calc_again= true;
					}
				}
			}
		}*/

		//System.out.println(expr);


		//Evaluate operations...

		char operators[]= {'^', '/', '*', '+', '-'};
		int no_of_operators[]= new int[operators.length];
		for(int i=0; i<no_of_operators.length; i++) {
			no_of_operators[i]=0;
		}

		for(int i=0; i<expr.length(); i++) {
			for(int j=0; j<operators.length; j++) {
				if(expr.charAt(i)== operators[j]) {
					no_of_operators[j]++;
					break;
				}
			}
		}

		String oper1, oper2;

		while(no_of_operators[0]>0) { //for all ^ operators
			oper1= "";
			oper2= "";
			boolean operator_found= false;
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)== '^' && !operator_found) {
					operator_found= true;
					continue;
				}
				if(!operator_found) {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-')
						oper1= "";
					else
						oper1= oper1+expr.charAt(i);
				}
				else {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-' || expr.charAt(i)== '^') {
						break;
					}
					else {
						oper2= oper2+expr.charAt(i);
					}
				}
			}
			String sub_expr= oper1+'^'+oper2;
			oper1= oper1.replace("~", "-");
			oper2= oper2.replace("~", "-");
			double sub_res= Math.pow(Double.parseDouble(oper1), Double.parseDouble(oper2));
			if(sub_res<0)
				expr= expr.replaceFirst(Pattern.quote(sub_expr), "~"+(0-sub_res));
			else
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
			expr= this.remove_e(expr);
			//System.out.println(oper1 + "  ^  "+ oper2);
			no_of_operators[0]--;


			
		}

		while(no_of_operators[1]>0) { //for all / operators
			oper1= "";
			oper2= "";
			boolean operator_found= false;
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)== '/' && !operator_found) {
					operator_found= true;
					continue;
				}
				if(!operator_found) {
					if(expr.charAt(i)== '^' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-')
						oper1= "";
					else
						oper1= oper1+expr.charAt(i);
				}
				else {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-' || expr.charAt(i)== '^') {
						break;
					}
					else {
						oper2= oper2+expr.charAt(i);
					}
				}
			}
			String sub_expr= oper1+'/'+oper2;
			oper1= oper1.replace("~", "-");
			oper2= oper2.replace("~", "-");
			double sub_res= Double.parseDouble(oper1)/ Double.parseDouble(oper2);
			if(sub_res<0)
				expr= expr.replaceFirst(Pattern.quote(sub_expr), "~"+(0-sub_res));
			else
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
			expr= this.remove_e(expr);
			//System.out.println(oper1 + "  /  "+ oper2);
			no_of_operators[1]--;


			
		}

		while(no_of_operators[2]>0) { //for all * operators
			oper1= "";
			oper2= "";
			boolean operator_found= false;
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)== '*' && !operator_found) {
					operator_found= true;
					continue;
				}
				if(!operator_found) {
					if(expr.charAt(i)== '^' || expr.charAt(i)== '/' || expr.charAt(i)== '+' || expr.charAt(i)== '-')
						oper1= "";
					else
						oper1= oper1+expr.charAt(i);
				}
				else {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-' || expr.charAt(i)== '^') {
						break;
					}
					else {
						oper2= oper2+expr.charAt(i);
					}
				}
			}
			String sub_expr= oper1+'*'+oper2;
			oper1= oper1.replace("~", "-");
			oper2= oper2.replace("~", "-");
			double sub_res= Double.parseDouble(oper1)* Double.parseDouble(oper2);
			if(sub_res<0)
				expr= expr.replaceFirst(Pattern.quote(sub_expr), "~"+(0-sub_res));
			else
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
			expr= this.remove_e(expr);
			//System.out.println(oper1 + "  *  "+ oper2);
			no_of_operators[2]--;


			
		}

		while(no_of_operators[3]>0) { //for all + operators
			oper1= "";
			oper2= "";
			boolean operator_found= false;
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)== '+' && !operator_found) {
					operator_found= true;
					continue;
				}
				if(!operator_found) {
					if(expr.charAt(i)== '^' || expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '-')
						oper1= "";
					else
						oper1= oper1+expr.charAt(i);
				}
				else {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-' || expr.charAt(i)== '^') {
						break;
					}
					else {
						oper2= oper2+expr.charAt(i);
					}
				}
			}
			String sub_expr= oper1+'+'+oper2;
			oper1= oper1.replace("~", "-");
			oper2= oper2.replace("~", "-");
			double sub_res= Double.parseDouble(oper1)+ Double.parseDouble(oper2);
			if(sub_res<0)
				expr= expr.replaceFirst(Pattern.quote(sub_expr), "~"+(0-sub_res));
			else
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
			//System.out.println(oper1 + "  +  "+ oper2);
			no_of_operators[3]--;
		}

		while(no_of_operators[4]>0) { //for all - operators
			oper1= "";
			oper2= "";
			boolean operator_found= false;
			for(int i=0; i<expr.length(); i++) {
				if(expr.charAt(i)== '-' && !operator_found) {
					operator_found= true;
					continue;
				}
				if(!operator_found) {
					if(expr.charAt(i)== '^' || expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+')
						oper1= "";
					else
						oper1= oper1+expr.charAt(i);
				}
				else {
					if(expr.charAt(i)== '/' || expr.charAt(i)== '*' || expr.charAt(i)== '+' || expr.charAt(i)== '-' || expr.charAt(i)== '^') {
						break;
					}
					else {
						oper2= oper2+expr.charAt(i);
					}
				}
			}
			String sub_expr= oper1+'-'+oper2;
			oper1= oper1.replace("~", "-");
			oper2= oper2.replace("~", "-");
			double sub_res= Double.parseDouble(oper1)- Double.parseDouble(oper2);
			if(sub_res<0)
				expr= expr.replaceFirst(Pattern.quote(sub_expr), "~"+(0-sub_res));
			else
				expr= expr.replaceFirst(Pattern.quote(sub_expr), ""+sub_res);
			//System.out.println(oper1 + "  -  "+ oper2);
			no_of_operators[4]--;
		}

		//System.out.println(expr);
		if(expr.charAt(0)== '~')
			expr= '-'+ expr.substring(1);
		

		
		return Double.parseDouble(expr);
	}

	private String get_val_from(String expr, int pos) {
		String res= ""+expr.charAt(pos);
		char trigonometric_operators[]= {'s', 'c', 't', 'S', 'C', 'T'};
		char operators[]= {'^', '/', '*', '+', '-'};
		boolean ended= false;

		for(int i=pos+1; i<expr.length() && !ended; i++) {

			for(int j=0; j<operators.length && !ended; j++) {
				if(operators[j]== expr.charAt(i)) {
					ended= true;
				}
			}
			for(int j=0; j<trigonometric_operators.length && !ended; j++) {
				if(trigonometric_operators[j]== expr.charAt(i)) {
					ended= true;
				}
			}
			if(!ended) {
				res= res+expr.charAt(i);
			}
		}
		return res;
	}

	private boolean is_a_number(char c) {
		char numbers[]= {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', 'E'};
		boolean is_a_number= false;
		for(int i=0; i<numbers.length; i++)
			if(c== numbers[i]) {
				is_a_number= true;
				break;
			}
		return is_a_number;
	}

	private static boolean is_a_number_wo_e(char c) {
		char numbers[]= {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.'};
		boolean is_a_number= false;
		for(int i=0; i<numbers.length; i++)
			if(c== numbers[i]) {
				is_a_number= true;
				break;
			}
		return is_a_number;
	}

	private String remove_e(String expr) {

		boolean e_found= false;
		String cu_priv= "", cu_next= "";

		for(int cu=0; cu<expr.length(); cu++) {
			if(is_a_number_wo_e(expr.charAt(cu))) {
				if(e_found)
					cu_next+= expr.charAt(cu);
				else
					cu_priv+= expr.charAt(cu);
			}
			else if(expr.charAt(cu)== '-' && e_found && cu_next.length()<1) {
				cu_next+='-';
				//System.out.println("\t HERE - ...");
			}
			else if(expr.charAt(cu)== '~' && e_found && cu_next.length()<1) {
				cu_next+='-';
				//System.out.println("\tHERE ~ ...");
			}
			else if(expr.charAt(cu)=='E') {
				e_found= true;
				//System.out.println("\te_found");
			}
			else {
				if(e_found) {
					double value= Double.parseDouble(cu_priv+'E'+cu_next);
					expr= expr.replaceFirst(Pattern.quote(cu_priv+'E'+cu_next), String.format("%.8f", value));
					e_found= false;
					cu_priv= "";
					cu_next= "";
					cu= -1;
				}
				else {
					e_found= false;
					cu_priv= "";
					cu_next= "";
				}
			}
		}
		if(e_found) {
			double value= Double.parseDouble(cu_priv+'E'+cu_next);
			expr= expr.replaceFirst(Pattern.quote(cu_priv+'E'+cu_next), String.format("%.8f", value));
			e_found= false;
			cu_priv= "";
			cu_next= "";
		}

		return expr;
	}

	private int fact(int n) {
		if(n==1 || n==0)
			return 1;
		return n*fact(n-1);
	}

	private int find_combination(int n, int r) {
		return (this.fact(n)/(this.fact(r)*this.fact(n-r)));
	}

	private int find_permutation(int n, int r) {
		return (this.fact(n)/this.fact(n-r));
	}

	private String formatExpression(String expr) {

		// for Constants...
		expr= expr.replace("PI", Math.PI+"");
		expr= expr.replace("e", Math.exp((double) 1)+"");
		expr= expr.replace("EXP", "10^");
		expr= expr.replace("RND", Math.random()+"");


		// for format according to eval...
		expr= expr.replace("aSin", "S");
		expr= expr.replace("aCos", "C");
		expr= expr.replace("aTan", "T");

		//expr= expr.replace("p", "p");
		expr= expr.replace("c", "b");

		expr= expr.replace("Sin", "s");
		expr= expr.replace("Cos", "c");
		expr= expr.replace("Tan", "t");
	
		expr= expr.replace("ln", "L");

		return expr;
	}

}

