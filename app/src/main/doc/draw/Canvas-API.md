## scale的两种方式
- scale(float sx, float sy)
前乘当前矩阵，同时放大宽高
- scale(float sx, float sy, float px, float py)
先translate到(px,py)点 再前乘当前矩阵，然后在此矩阵的前提下，
再translate到(-px,-py)点,实际上已经是tarnslate(- sx * px, -sy * py)。
等同于：
translate(px, py);
scale(sx, sy);
translate(-px, -py);