package page;

public class Node implements Comparable<Node> {
    private int x;
    private int y;
    private int player;
    private int orderNumber;
    private int num;

    public Node(int x, int y, int player, int orderNumber) {
        super();
        this.x = x;
        this.y = y;
        this.orderNumber = orderNumber;
        this.player = player;
    }

    @Override
    public int compareTo(Node node) {
        if (this.num > node.num) {
            return -1;
        } else if (this.num < node.num) {
            return 1;
        } else
            return 0;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
