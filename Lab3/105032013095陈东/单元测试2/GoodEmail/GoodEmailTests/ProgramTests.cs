using Microsoft.VisualStudio.TestTools.UnitTesting;
using GoodEmail;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GoodEmail.Tests
{
    [TestClass()]
    public class ProgramTests
    {
        [TestMethod()]
        public void vertifyTest1()
        {
            bool result = Program.vertify("11@aa.com");
            Assert.IsTrue(result);
        }

        [TestMethod()]
        public void vertifyTest2()
        {
            bool result = Program.vertify("");
            Assert.IsFalse(result);
        }

        [TestMethod()]
        public void vertifyTest3()
        {
            bool result = Program.vertify("11@.com");
            Assert.IsFalse(result);
        }

        [TestMethod()]
        public void vertifyTest4()
        {
            bool result = Program.vertify("11aa.com");
            Assert.IsFalse(result);
        }

        [TestMethod()]
        public void vertifyTest5()
        {
            bool result = Program.vertify("11@aacom");
            Assert.IsFalse(result);
        }
    }
}