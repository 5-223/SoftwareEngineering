using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;



namespace DemoUser
{
    public class User
    {
        private String m_email;

        public User(string userEmail)
        {
            m_email = userEmail;
            if (userEmail == "")
            {
                m_email = "empty";
            }
        }

        public bool show()
        {
            Console.WriteLine(m_email);
            return false;
        }


        public int sum(int a, int b)
        {
            return a + b;
        }

        /// <summary>
        /// 数据接口的测试
        /// </summary>
       
        public bool testInterface(int a, int b, string c)
        {
           
            if (c == "")
                return true;
            
            else
                return false;
        }
  
        public bool testBorder(int border)
        {

            for (int i = 50; i < border; i++)
            {
                Console.WriteLine("尚未越界");
                return true;
            }
            Console.WriteLine("越界");
            return false;

        }
    }

   
}
