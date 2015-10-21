using Microsoft.VisualStudio.TestTools.UnitTesting;
using Test_1;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Test_1.Tests
{
    [TestClass()]
    public class ProgramTests
    {
        [TestMethod()]
        public void Tel1Test()
        {
            bool result = Program.Tel1("13165124785");

            Assert.IsTrue(result);
        }

        [TestMethod()]
        public void Tel2Test()
        {
            bool result = Program.Tel2("18865124785");

            Assert.IsTrue(result);
        }

        [TestMethod()]
        public void Tel3Test()
        {
            bool result = Program.Tel3("17765124785");

            Assert.IsTrue(result);
        }
    }
}