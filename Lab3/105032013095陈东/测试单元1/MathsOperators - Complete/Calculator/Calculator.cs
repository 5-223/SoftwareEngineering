using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyMath
{
    public class Calculator
    {
        public Calculator(char op)
        {
            this.op = op;
        }
        private char __op;
        public char op
        {
            get
            {
                return __op;
            }
            set
            {
                if (value == '+' || value == '-' ||
                    value == '*' || value == '/')
                {
                    __op = value;
                }
                else
                {
                    __op = '\0';
                }
            }
        }

        public double calc(double a, double b)
        {
            switch(op)
            {
                case '+':
                    return a + b;
                case '-':
                    return a - b;
                case '*':
                    return a * b;
                case '/':
                    return a / b;
                default:
                    return 0;
            }
        }
    }
}
