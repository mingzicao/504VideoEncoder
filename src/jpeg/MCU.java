package jpeg;

/* Block = 8*8
 * MCU 是一个最小计算采样单元； 由16 * 16px 组成 所以有4个block;
 * 所以 MCU 是一个 2 * 2 的 BlockGroup;
 * 按照不同的sampling ratio, Y：Cr: Cb 的取样block的个数不同
 * 再 分别把 Block存 成三个1D数组；
 * */

public class MCU {
	public int SamplingRatio;
	Block[][] BlockGroup;
	Block[] Y;  // 根据不同sampling Ratio, Y：Cr: Cb的取样不同；BlockArray 的长度不同；
	Block[] Cr;
	Block[] Cb;
	
	
	
}
