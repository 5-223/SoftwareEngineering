using Microsoft.VisualStudio.TestTools.UnitTesting;
using Test_2;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test_2.Tests
{
    [TestClass()]
    public class ProgramTests
    {
        [TestMethod()]
        public void AddTest()
        {
            int result = Program.Add(2, 5);

            Assert.IsTrue(result == 7);
        }

        [TestMethod()]
        public void DecTest()
        {
            int result = Program.Dec(5,2);

            Assert.IsTrue(result == 3);
        }

        [TestMethod()]
        public void MulTest()
        {
            int result = Program.Mul(2, 5);

            Assert.IsTrue(result == 10);
        }

        [TestMethod()]
        public void DivTest()
        {
            int result = Program.Div(4, 2);

            Assert.IsTrue(result == 2);
        }
    }
}