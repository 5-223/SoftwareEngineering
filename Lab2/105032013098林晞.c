#include <reg52.h>

sbit ADDR0 = P1^0;
sbit ADDR1 = P1^1;
sbit ADDR2 = P1^2;
sbit ADDR3 = P1^3;
sbit ENLED = P1^4;

sbit KeyOut4 = P2^0;
sbit KeyOut3 = P2^1;
sbit KeyOut2 = P2^2;
sbit KeyOut1 = P2^3;
sbit KeyIn1 = P2^4;
sbit KeyIn2 = P2^5;
sbit KeyIn3 = P2^6;
sbit KeyIn4 = P2^7;

// 0 1 2 ... 9 A b ... F
unsigned char Digit[] = {
	~0x3f, ~0x06, ~0x5b, ~0x4f, ~0x66, ~0x6d, ~0x7d, //0~6
	~0x07, ~0x7f, ~0x6f, ~0x77, ~0x7c, ~0x39, //7~C
	~0x57, ~0x75//d, E
};

//矩阵按键编号到ascii码的映射表
unsigned char code KeyCodeMap[4][4] = { 
    { 0x31, 0x32, 0x33, 0x26 }, //数字键1、数字键2、数字键3、向上键
    { 0x34, 0x35, 0x36, 0x25 }, //数字键4、数字键5、数字键6、向左键
    { 0x37, 0x38, 0x39, 0x28 }, //数字键7、数字键8、数字键9、向下键
    { 0x30, 0x1B, 0x0D, 0x27 }  //数字键0、ESC键、  回车键、 向右键
};

unsigned char LedBuffer[6] = {0xff, 0xff, 0xff, 0xff, 0xff, 0xff};

void InitLed();
void InitTimer0();
void ShowNumber(long);

void main()
{
	P2 = 0xF7;
	InitLed();
	InitTimer0();
	while(1)
		;
}

void KeyAction(unsigned char key)
{
	static long number = 0;
	static long result = 0;
	static char op = '\0';

	if (key >= '0' && key <= '9') {
		//number
		number = 10*number + key - '0';
		ShowNumber(number);
	} else if (key == 0x0D) {
		//enter
		if (op == '+') {
			result += number;
		} else if (op == '-') {
			result -= number;
		} else if (op == '\0') {
			result = number;
		}
		number = 0;
		ShowNumber(result);
	} else if (key == 0x1B) {
		//esc
		result = number = 0;
		op = '\0';
		ShowNumber(0);
	} else if (key == 0x26) {
		//plus
		if (op == '+') {
			result += number;
		} else if (op == '-') {
			result -= number;
		} else if (op == '\0') {
			result = number;
		}
		op = '+';
		number = 0;
		ShowNumber(result);
	} else if (key == 0x28) {
		//minus
		if (op == '+') {
			result += number;
		} else if (op == '-') {
			result -= number;
		} else if (op == '\0') {
			result = number;
		}
		op = '-';
		number = 0;
		ShowNumber(result);
	}
}

void KeyScan()
{
	static unsigned char keyStatusBuffer[4][4] = {
		{0xff, 0xff, 0xff, 0xff}, {0xff, 0xff, 0xff, 0xff},
		{0xff, 0xff, 0xff, 0xff}, {0xff, 0xff, 0xff, 0xff}
	};
	static unsigned char keyStatus[4][4] = {
		{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}
	};
	static unsigned char keyBackup[4][4] = {
		{1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}
	};
	static int i = 0;
	int j;
	for (j = 0; j < 4; j++) {
		bit tmp = P2 & (0x10<<j);
		keyStatusBuffer[i][j] = (keyStatusBuffer[i][j] << 1) | tmp;
		if (keyStatusBuffer[i][j] == 0xff) {
			keyStatus[i][j] = 1;
		}
		else if (keyStatusBuffer[i][j] == 0x00) {
			keyStatus[i][j] = 0;
		}
		if (keyBackup[i][j] != keyStatus[i][j]) {
			if (keyBackup[i][j] == 0) {
				KeyAction(KeyCodeMap[i][j]);
			}
			keyBackup[i][j] = keyStatus[i][j];
		}
	}
	i = (i+1)%4;
	P2 = 0xff ^ (1<<(3-i));
}

void ShowNumber(long a)
{
	//a must in [-99999, 99999]
	int i;
	char isNegative = (a < 0);

	if (a < -99999 || a > 99999)
		goto show_error;

	a *= isNegative ? -1 : 1;

	for (i = 0; i < 6; i++) {
		LedBuffer[i] = Digit[a%10];
		a /= 10;
	}

	for (i = 5; i >= 1; i--) {
		if (LedBuffer[i] != Digit[0])
			break;
		else
			LedBuffer[i] = 0xff;
	}

	LedBuffer[i+1] = isNegative ? ~0x40 : 0xff;
	return;
show_error:
	LedBuffer[0] = LedBuffer[1] = ~0x31;
	LedBuffer[2] = ~0x79;
	LedBuffer[3] = LedBuffer[4] = LedBuffer[5] = 0xff;
}

void InitTimer0()
{
	EA = 1;
	ET0 = 1;
	TR0 = 1;
	TMOD = 0x01;
	TH0 = 0xFC;
	TL0 = 0x67;
}

void InitLed()
{
	ENLED = 0;
	ADDR3 = 1;
}

void LedScan()
{
	static int i = 0;
	P0 = 0xff;
	ADDR0 = i & 1; ADDR1 = i & 2; ADDR2 = i & 4;
	P0 = LedBuffer[i];
	i = (i + 1) % 6;
}

void InterruptTimer0() interrupt 1
{
	TH0 = 0xFC;
	TL0 = 0x67;
	KeyScan();
	LedScan();
}
