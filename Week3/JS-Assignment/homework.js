/* -----------------------------------------------------------------------------------
PART I

Create a single Javascript file called homework.js to answer these questions.
Please put the question itself as a comment above each answer.
----------------------------------------------------------------------------------- */


/*
*   Part 1
*/
    
/*  1. Fibonacci
Define function: fib(n) 
Return the nth number in the fibonacci sequence. */
function fib(n){
    let prev = 1;
    let curr = 1;
    let result = 0;
    
    if (n >= 0){
        if (n == 0){
            return 0;
        }
        if (n == 1 || n == 2){
            return 1;
        }
        for (i = 3; i <= n; i++){
            result = prev+curr;
            prev = curr;
            curr = result;
        }
        return result;
    }
}

console.log(fib(16));

/* 2. Bubble Sort
Define function: bubbleSort(numArray)
Use the bubble sort algorithm to sort the array.
Return the sorted array. */

function bubbleSort(numArray){
    let temp = 0;
    let sorted;

    for(i = 0; i < numArray.length - 1; i++){
        sorted = true;

        for(j = 0; j < numArray.length - i + 1; j++){
            if (numArray[j] > numArray[j + 1]){
                temp = numArray[j];
                numArray[j] = numArray[j+1];
                numArray[j + 1] = temp;
                sorted = false;
            } 
        }

        if (sorted){
            break;
        }
    }
    return numArray;
}

console.log(bubbleSort([4,3,2,8,5,1]));

/* 3. Reverse String
Define function: reverseStr(someStr)
Reverse and return the String. */

let reverseStr = function(someStr){
    return someStr.split("").reverse().join("");
}

console.log(reverseStr("Hello Nick!"));

/* 4. Factorial
Define function: factorial(someNum)
Use recursion to compute and return the factorial of someNum. */

let factorial = function(someNum){
    
    if (someNum === 1){
      return someNum;
    } 
    return someNum*factorial(someNum-1);
}

console.log(factorial(7));

/* 5. Substring
Define function substring(someStr, length, offset)
Return the substring contained between offset and (offset + length) inclusively.
If incorrect input is entered, use the alert function and describe why the input was incorrect. */

let substr = function(someStr, length, offset){
    
    //Assuming offset is start index (of 0) and length is how many indecies we move
    // "HeLLo" 2 , 2 =>  [1] = 'L' [2] = 'L' "LL" -- 2 letter offset with a length of 2 letters
    let array = someStr.split("");
    let newStr = "";

    if ((length+offset) > array.length || length > array.length){
        alert("Ending index is greater than the length of the word");
    }

    if (offset > array.length){
        alert("Starting index is greater than length of the word");
    }

    if (offset < 0 || length < 0){
        alert("Error negative values will not be accepted");
    }

    for(i = offset; i < offset + length; i++){
        newStr += array[i];
    }

    return newStr;

}

console.log(substr("abcde", 3, 1)); //Prints [1] = b , [2] = c, [3] = d This is an offset of 1 and a length of 3 characters 

/* 6. Even Number
Define function: isEven(someNum)
Return true if even, false if odd.
Do not use % operator. */

let isEven = function (someNum){
    
    if(Math.abs(someNum) == 1){
        return false;
    }

    someNum = Math.abs(someNum)/2; 
    return (!(someNum - Math.floor(someNum)>0));
}

/* 7. Palindrome
Define function isPalindrome(someStr)
Return true if someStr is a palindrome, otherwise return false */

let isPalindrome = function(someStr){
    return someStr == reverseStr(someStr);
}
console.log(isPalindrome("michael"));
console.log(isPalindrome("racecar"));


/* 8. Shapes
Define function: printShape(shape, height, character)
shape is a String and is either "Square", "Triangle", "Diamond".
height is a Number and is the height of the shape. Assume the number is odd.
character is a String that represents the contents of the shape. Assume this String contains just one character.
Use a switch statement to determine which shape was passed in.
Use the console.log function to print the desired shape.
Example for printShape("Square", 3, "%");
%%%
%%%
%%%
Example for printShape("Triangle", 3, "$");
$
$$
$$$
Example for printShape("Diamond", 5, "*");
  *
 ***
*****
 ***
  * */

function printShape(shape, height, character){
    let shapeStr = "";

    switch (shape){
        case "Square":
            for (i = 0; i < height; i++){
                for (j = 0; j < height; j++){
                    shapeStr += character;
                    if (j == height-1){
                        shapeStr += "\n";
                    }
                }
           }
            break;
        case "Triangle":
            for (i = 0; i < height; i++){
                for (j = 0; j < i+1; j++){
                    shapeStr += character;
                }
                if (i != height -1){
                    shapeStr += "\n";
                }
           }
            break;
        case "Diamond":
            half = Math.floor(height/2);
            counter = 0;

            for (i = 0; i < height; i++){
                for (j = 0; j < height; j++){
                    
                    if (i == half){
                    shapeStr += character;
                    counter = 0;
                    }
                    else if (i < half){
                        if (j < half-counter || j > half+counter){
                            shapeStr += " ";
                        }
                        else{
                            shapeStr += character;
                        }
                    }
                    else{
                        if (j < counter || j > height-1-counter){
                            shapeStr += " ";
                        }
                        else{
                            shapeStr += character;
                        }
                    }
                }
                counter ++;
                shapeStr += "\n";
           }
            break;
    } 
    return shapeStr;
}
console.log(printShape("Square", 3, "#"));
console.log(printShape("Triangle", 5, "?"));
console.log(printShape("Diamond", 7, "$"));


/* 9. Object literal
Define function traverseObject(someObj)
Print every property and it's value. */

function traverseObject (someObj){

    let result = "";
    let keys = Object.keys(someObj);
    
    for (i in obj){  
        result +=  i +" is "+someObj[i]+"\n";
    }
    return result;
}

let obj = {name: "Michael", age: 29, isAwesome: true };
console.log(traverseObject(obj));

/* 10. Delete Element
Define function deleteElement(someArr)
Print length
Delete the third element in the array.
Print length
The lengths should be the same. */

function deleteElement(someArr){
    console.log(someArr.length);
    delete someArr[2];
    console.log(someArr);
    console.log(someArr.length);
}

console.log(deleteElement([4,3,2,8,5,1]));
/* 11. Splice Element
Define function spliceElement(someArr)
Print length
Splice the third element in the array.
Print length
The lengths should be one less than the original length. */

function spliceElement(someArr){
    console.log(someArr.length);
    someArr.splice(2,1);  
    console.log(someArr);
    console.log(someArr.length);
}

console.log(spliceElement([4,3,2,8,5,1]));

/* 12. Defining an object using a constructor
Define a function Person(name, age)
The following line should set a Person object to the variable john:*/

function Person (name, age){
    this.name = name;
    this.age = age;
}

let john = new Person("John", 30); 
console.log(john);

/* 13. Defining an object using an object literal
Define function getPerson(name, age)
The following line should set a Person object to the variable john:*/

function getPerson(name, age){
    let newPerson = {
        name: name,
        age: age
    };
    return newPerson;
}

let john2 = getPerson("John", 30); 
console.log(john2); 

