using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test_1
{
    public class Program
    {
        public static bool Tel1(string a)
        {
            const string regPattern = @"^(130|131|132|133|134|135|136|137|138|139)\d{8}$";
            return System.Text.RegularExpressions.Regex.IsMatch(a,regPattern);
        }
        public static bool Tel2(string a)
        {
            const string regPattern = @"^(180|181|182|183|184|185|186|187|188|189)\d{8}$";
            return System.Text.RegularExpressions.Regex.IsMatch(a, regPattern);
        }
        public static bool Tel3(string a)
        {
            const string regPattern = @"^(170|171|172|173|174|175|176|177|178|179)\d{8}$";
            return System.Text.RegularExpressions.Regex.IsMatch(a, regPattern);
        }
        static void Main(string[] args)
        {

        }
    }
}
