using DemoUser;
using Microsoft.VisualStudio.TestTools.UnitTesting;
namespace TestProject1
{
    
    
    /// <summary>
    ///这是 UserTest 的测试类，旨在
    ///包含所有 UserTest 单元测试
    ///</summary>
    [TestClass()]
    public class UserTest
    {


        private TestContext testContextInstance;

        /// <summary>
        ///获取或设置测试上下文，上下文提供
        ///有关当前测试运行及其功能的信息。
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return testContextInstance;
            }
            set
            {
                testContextInstance = value;
            }
        }

        #region 附加测试属性
        // 
        //编写测试时，还可使用以下属性:
        //
        //使用 ClassInitialize 在运行类中的第一个测试前先运行代码
        //[ClassInitialize()]
        //public static void MyClassInitialize(TestContext testContext)
        //{
        //}
        //
        //使用 ClassCleanup 在运行完类中的所有测试后再运行代码
        //[ClassCleanup()]
        //public static void MyClassCleanup()
        //{
        //}
        //
        //使用 TestInitialize 在运行每个测试前先运行代码
        //[TestInitialize()]
        //public void MyTestInitialize()
        //{
        //}
        //
        //使用 TestCleanup 在运行完每个测试后运行代码
        //[TestCleanup()]
        //public void MyTestCleanup()
        //{
        //}
        //
        #endregion


        /// <summary>
        ///User 构造函数 的测试
        ///</summary>
        [TestMethod()]
        public void UserConstructorTest()
        {   
            string userEmail = "123@qq.com"; // TODO: 初始化为适当的值
            User target = new User(userEmail);
       
            Assert.IsNotNull(target);
            //Assert.IsTrue(sum(3, 5) == 8)
        }

        /// <summary>
        ///User 构造函数 的测试
        ///</summary>
        [TestMethod()]
        public void UserConstructorEmptyTest()
        {
            string userEmail = ""; // 
            User target = new User(userEmail);

            Assert.IsNotNull(target);
            //Assert.IsTrue(sum(3, 5) == 8)
        }

        /// <summary>
        ///show函数 的测试
        ///</summary>
        [TestMethod()]
        public void ShowTest()
        {
            User u = new User("123");
            Assert.IsTrue(u.show());
        }


        /// <summary>
        ///2数据接口的测试
        ///</summary>
        [TestMethod()]
        public void InterfaceTest()
        {
            User u = new User("123");
            //u.testInterface(1,2,"abc");
            Assert.IsTrue(u.testInterface(1,2,"abc"));
        }

        /// <summary>
        ///3边界的测试
        ///</summary>
        [TestMethod()]
        public void BorderTest()
        {
            User u = new User("123");
            //u.testInterface(1,2,"abc");
            Assert.IsTrue(u.testBorder(10));
        }
        
    }
}
