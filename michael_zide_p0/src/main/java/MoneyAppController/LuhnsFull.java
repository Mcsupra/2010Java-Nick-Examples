package MoneyAppController;

public class LuhnsFull {
	
	public static int LuhnsAlgo(String cardNum) {
		String test = cardNum;
		test = test.replaceAll("[0-9]","").replaceAll("\\s", "");
		cardNum = cardNum.replaceAll("[^0-9]","");
		int cardLength = cardNum.length();
		
		if (test.length() != 0) {
			return 0;
		}
		
		
		  if (cardLength == 13 || cardLength == 15 || cardLength == 16){ //Only run this code for valid card lengths
		    
		    
			int count = cardNum.length();
			long by2 = 0;
			long check = 0;
			long notby2 = 0;
			long temp_num = Long.parseLong(cardNum);
			long number = temp_num;
			long numberType = temp_num;  
			
			if (count % 2 == 0) //Checking if count is even or odd
			{
			       for (int i = 0; i < count / 2; i++) //Since we'll be moving through the number 2 digits at a time, 
			           //starting with 2nd from last, we only need 1/2 count
			       {
			           check = ((number % 100 / 10) * 2); // We need to check if twice the digit is 2 digits long
			           
			           if (check >= 10) //If it is, do this.
			           {
			               for (int j = 0; j < 2; j++)
			               {
			                   by2 = by2 + (check % 10); //We need to add the DIGITS of the 2 digit number,
			                   //not the number itself. 2 * 6 is 12 is 1 + 2
			                   check = check / 10;       //Get both digits individually
			               }
			           }
			           else
			           {
			               by2 = by2 + check;  //If the number is less than 10, store it in this variable "by2"
			           }
			           number = number / 100;  //Move to the next pair of digits
			        }
			        
			    }
			    
			    else
			    {
			        count = count - 1; //If count is odd, make it even
			        for (int i = 0; i < count / 2; i++)
			        {
			            check = ((number % 100 / 10) * 2);  //Same code as above from here down
			            
			            if (check >= 10)
			            {
			                for (int j = 0; j < 2; j++)
			                {
			                    by2 = by2 + (check % 10);
			                    check = check / 10;
			                }
			            }
			            else
			            {
			                by2 = by2 + check;
			            }
			            number = number / 100;
			            
			        }
			        
			    }
			    
			    
			    for (int k = 0; k <= count / 2; k++) 
			    {
			        notby2 = notby2 + temp_num % 10; //Now we pull out the digits we didn't use and and add them together
			        temp_num = temp_num / 100;
			    }
			    
			    long validate = by2 + notby2;  //Last piece of Luhn's algorithm 
			    
			    if (validate == 0){ //If the length is valid, but it fails Luhn's... throw it out
		        
		            System.out.println("INVALID\n");
		        }   
		        else{ // If it passes Luhn's checksum, do this
		        
		        	int type;
		         
		            if (cardLength % 2 == 0)
		            {
		                for (int i = 0; i < cardLength / 2 - 1; i++) //We want to leave the remaining 2 digits
		                {
		                    numberType = numberType / 100;
		                }
		            }
		            
		            else
		            {
		                numberType = numberType / 10;
		                cardLength = cardLength - 3;  //Change count to account for count being odd, otherwise, same code
		                for (int i = 0; i < cardLength / 2; i++)
		                {
		                    numberType = numberType / 100;
		                }
		            }
		             
		            type = (int) numberType;
		            
		            return type;
		            
		        }
			   
		}
		  return 0;
		  
	}
}
