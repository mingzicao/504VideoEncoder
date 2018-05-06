# Documentation
## Description of Problem
In this project, we encoding upto 100 images into a econding file which sized have been compressed, then decoding this file and generate a movie based on the reconstruct images.

First of all ,you can go to src/GUI to open GUI3.java to run our GUI. The detail insturction please follow our GUIDE:"Instruction one Vedio Encoder".

## Insturction
All the guide have been write into the "Instrcution on Veido Encoder", please follow this document to run our project. Also we have provide the guide vedio so that you can take a look:
* guide vedio for commandline : cmldemo
* guide vedio for GUI: GUIdemo.

What I need to mention is that the GUI3 is under src/GUI, not the root path's GUI folder.

## Group Member
* mingzi cao
* muzi li
* jiahao zhang
* zhengxiang zhong


## Implementation

### GUI 3
It creates a JFrame that initiates buttons, Labels and Fields for user inputs. By using button Action Listener to call the different function in the other class.

### VedioEncoder test
It creats the commandline to let user can give command to encoding file and play video.

### ReadImages_1
The purpose of this Class is to write the compressed data into byte stream. This class will read all images then based on their different type  to do the different compression process, then output a encoding file.

### Decode
The purpose of this Class is to decoding the encoding file and output a list of images.

### SizeTrimer
This class mainly resize the image, once the size of image is not the times of 16*16, we need to resize it, so that our MCU unit can be work. Each time we read a image, we will call this class to initial the new size of image. 

### YuvImage
This class initial the class of Yuv, and provide the method about yuvtoRGB transfer and RGBtoYuv trasnfer.

### Block,MCU
These two classes initial the class of blocks and the class of MCU, these two classes will provide convenient to other class  when dealing with the images. During the compression process, the information in data will be stored in these two kinds of class.

### ImageGrid,Sampler
These two class main Grid the image into MCU types, then based on the different sampling ratio to compress the blocks in MCU.

### MCU
Initial the class of MCU, sampinge blocks based on different sampling ration.

### DCT
The DCT method takes a block and performs DCT. The DCT is used to remove high frequency noise within the image and assist with the inter frame compression. 

### Quantizer
Provide a quantization table determined by the quality setting input from the GUI, transfer the DCT result into a int.

### RunLengthEncoder,HuffmanEncoder
Based on the result of quantizer, do the RLE for the AC part, then do the Huffman encoding to generate the final encoding result.
### ImageEncoder
Based in the Huffman Encoding result to generate the images.

### FilestoMov
Based on the compressed images, generate movie by Jim2Mov lib.


## Reference
Gregory K. Wallace: THE JPEG STILL PICTURE COMPRESSION STANDARD, IEEE Transactions on Consumer Electronics, Vol. 38, No. 1, FEBRUARY 1992

Philip H.S. Torr and Andrew Zisserman: Feature Based Methods for Structure and Motion Estimation, ICCV Workshop on Vision Algorithms, pages 278-294, 1999         

Rui Xu, David Taubman & Aous Thabit Naman, 'Motion Estimation Based on Mutual Information and Adaptive Multi-scale Thresholding', in Image Processing, IEEE Transactions on, vol.25, no.3, pp.1095-1108, March 2016.  

Zhu, Shan; Ma, Kai-Kuang (February 2000). "A New Diamond Search Algorithm for Fast Block-Matching Motion Estimation". EEE Trans. Image Processing. 9 (12): 287–290.

Rosa A Asmara, Reza Agustina and Hidayatulloh, “Comparison of Discrete Cosine Transforms (DCT), Discrete Fourier Transforms (DFT), and Discrete Wavelet Transforms (DWT) in Digital Image Watermarking”, International Journal of Advanced Computer Science and Applications(IJACSA), 8(2), 2017.
