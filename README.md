# MikuHome
主要是关于一些OpenGL的demo，顺便会自己改编一些 比较有意思的自定义控件。

另外我前段时间由于工作需要研究关于如何用opengl实现仿真翻页效果。目前已知的demo效果都不理想，通过贝塞尔曲线实现的翻页效果缺少文字的扭曲效果，看起来没有立体感。我需要达到掌阅ireader的那种效果，在书页弯曲的地方字体也要有明显的扭曲效果。所以我开始研究关于android上opengl的一些应用，自己写了一些小demo。

![image](https://github.com/lord19871207/MikuHome/blob/master/AnimateTest/curl1.png)

![image](https://github.com/lord19871207/MikuHome/blob/master/AnimateTest/curl2.png)

用户看到的可以分为3部分：当前页的可见部分(下图绿色部分)，把书页翻起来后看到的背面区域(下图黄色部分)，把书页翻起来后看到的下一页的一角(下图绿色部分)。
假设我们已经求得了包含黄色区域和蓝色区域的Path, 假设为mPath0,那么绿色区域则可以使用

Canvas.clipPath(mPath0, Region.Op.XOR)来剪裁绘制；

而蓝色区域则可以通过使用(假设黄色区域的Path为mPath1)

Canvas.clipPath(mPath0);   

Canvas.clipPath(mPath1, Region.Op.DIFFERENCE); //绘制第一次不同于第二次的区域 
