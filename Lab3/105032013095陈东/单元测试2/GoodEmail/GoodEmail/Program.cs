using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;

namespace GoodEmail
{
    public class Program
    {
        public static bool vertify(String email)
        {
            string strRegex = @"^([a-zA-Z0-9_-]+)@([a-zA-Z0-9_-]+).([a-zA-Z0-9_-]+)$";
            Regex re = new Regex(strRegex);
            if (re.IsMatch(email))
                return (true);
            else
                return (false);
        }
        static void Main(string[] args)
        {
        }
    }
}
