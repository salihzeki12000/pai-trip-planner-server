package wykwon.common.old;

import org.apache.log4j.Logger;

public class OldDoubleCube {
    private static Logger logger = Logger.getLogger(OldDoubleCube.class);
    // depth, row, col ������ ������
    double array[][][];
    int depthSize;
    int rowSize;
    int colSize;

    /**
     * row ũ��� 0�� StringTable�� �ʱ�ȭ��
     *
     * @param header table�� column header��
     */
    public OldDoubleCube(int depthSize, int rowSize, int colSize) {
        this.depthSize = depthSize;
        this.rowSize = rowSize;
        this.colSize = colSize;
        array = new double[depthSize][rowSize][colSize];
    }

    public double getValue(int depthIdx, int rowIdx, int colIdx) {
        return array[depthIdx][rowIdx][colIdx];
    }

    public void setValue(int depthIdx, int rowIdx, int colIdx, double value) {
        array[depthIdx][rowIdx][colIdx] = value;
    }

    public double[][][] referWholeArray() {
        return array;
    }

    public void setRowVector(int depthIdx, int rowIdx, double data[]) {
        if (data.length != colSize) {
            throw new RuntimeException("column dimension mismatch at depth="
                    + depthIdx + " row=" + rowIdx);
        }
        for (int colIdx = 0; colIdx < data.length; colIdx++) {
            array[depthIdx][rowIdx][colIdx] = data[colIdx];
        }
    }

    public void setColVector(int depthIdx, int colIdx, double data[]) {
        if (data.length != rowSize) {
            throw new RuntimeException("column dimension mismatch at depth="
                    + depthIdx + " col=" + colIdx);
        }
        for (int rowIdx = 0; rowIdx < data.length; rowIdx++) {
            array[depthIdx][rowIdx][colIdx] = data[rowIdx];
        }
    }

    public void setDepthVector(int rowIdx, int colIdx, double data[]) {
        if (data.length != rowSize) {
            throw new RuntimeException("depth dimension mismatch at row="
                    + rowIdx + " col=" + colIdx);
        }
        for (int depthIdx = 0; depthIdx < data.length; depthIdx++) {
            array[depthIdx][rowIdx][colIdx] = data[depthIdx];
        }
    }

    public double[] getRowVector(int depthIdx, int colIdx) {
        double rowVector[] = new double[rowSize];
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            rowVector[rowIdx] = array[depthIdx][rowIdx][colIdx];
        }
        return rowVector;
    }

    public double[] getColVector(int depthIdx, int rowIdx) {
        double colVector[] = new double[colSize];
        for (int colIdx = 0; colIdx < colSize; colIdx++) {
            colVector[colIdx] = array[depthIdx][rowIdx][colIdx];
        }
        return colVector;
    }

    public double[] getDepthlVector(int rowIdx, int colIdx) {
        double depthVector[] = new double[depthSize];
        for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
            depthVector[depthIdx] = array[depthIdx][rowIdx][colIdx];
        }
        return depthVector;
    }

    public double[][] get2DArray_Row_Col(int depthIdx) {
        return array[depthIdx];
    }

    public void setMatrix_Row_Col(int depthIdx, DoubleMatrix matrix) {
        double arrays2D[][] = matrix.referArray();

        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                array[depthIdx][rowIdx][colIdx] = arrays2D[rowIdx][colIdx];
            }
        }
    }

    public int getDepthSize() {
        return depthSize;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getColSize() {
        return colSize;
    }

    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("depthSize=" + depthSize);
        strbuf.append("  rowSize=" + rowSize);
        strbuf.append("  colSize=" + colSize + "\n");
        for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
            strbuf.append("\ndepth=" + depthIdx + "  ================\n");
            strbuf.append(new DoubleMatrix(get2DArray_Row_Col(depthIdx)));
        }
        return strbuf.toString();
    }
}