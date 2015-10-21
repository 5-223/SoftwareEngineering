using Microsoft.VisualStudio.TestTools.UnitTesting;
using MyMath;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MyMath.Tests
{
    [TestClass()]
    public class AssemblyInfo
    {
        [TestMethod()]
        public void CalculatorTest()
        {
//            Assert.Fail();
        }

        /// <summary>
        /// 测试加法
        /// </summary>
        [TestMethod()]
        public void addTest()
        {
            Calculator calc = new Calculator('+');
            double result = calc.calc(3.3, 5.5);
            Assert.IsTrue(result == 8.8);
        }

        /// <summary>
        /// 测试减法
        /// </summary>
        [TestMethod()]
        public void subTest()
        {
            Calculator calc = new Calculator('-');
            double result = calc.calc(3.3, 5.5);
            Assert.IsTrue(result == -2.2);
        }

        /// <summary>
        /// 测试乘法
        /// </summary>
        [TestMethod()]
        public void multiTest()
        {
            Calculator calc = new Calculator('*');
            double result = calc.calc(3.3, 5.5);
            Assert.IsTrue(result == 18.15);
        }

        /// <summary>
        /// 测试除法
        /// </summary>
        [TestMethod()]
        public void divTest1()
        {
            Calculator calc = new Calculator('/');
            double result = calc.calc(3.3, 5.5);
            Assert.IsTrue(result == 0.6);
        }

        /// <summary>
        /// 测试除法（分母为0）
        /// </summary>
        [TestMethod()]
        public void divTest2()
        {
            Calculator calc = new Calculator('/');
            double result = calc.calc(3.3, 0);
            Assert.IsTrue(Double.IsInfinity(result));
        }
    }
}