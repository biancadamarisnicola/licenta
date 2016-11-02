package ANN;

import java.math.BigInteger;

/**
 * Created by bianca on 09.09.2016.
 */
public class Fraction {
    BigInteger denominator;
    BigInteger numerator;

    public Fraction(BigInteger numerator, BigInteger denominator) {
        BigInteger gcd = getGcd(numerator, denominator);
        this.denominator = denominator.divide(gcd);
        this.numerator = numerator.divide(gcd);
    }

    public Fraction(BigInteger numerator) {
        this.denominator = BigInteger.ONE;
        this.numerator = numerator;
    }

    public Fraction add(Fraction other) {
        BigInteger gcd = getGcd(this.denominator, other.denominator);
        BigInteger numerator1 = this.numerator.multiply(other.denominator.divide(gcd));
        BigInteger numerator2 = other.numerator.multiply(this.denominator.divide(gcd));
        BigInteger denominator1 = denominator.multiply(other.denominator.divide(gcd));
        return new Fraction(numerator1.add(numerator2), denominator1);
    }

    public Fraction sub(Fraction other) {
        BigInteger gcd = getGcd(this.denominator, other.denominator);
        BigInteger numerator1 = this.numerator.multiply(other.denominator.divide(gcd));
        BigInteger numerator2 = other.numerator.multiply(this.denominator.divide(gcd));
        BigInteger denominator1 = denominator.multiply(other.denominator.divide(gcd));
        return new Fraction(numerator1.subtract(numerator2), denominator1);
    }

    public Fraction mul(Fraction other) {
        return new Fraction(numerator.multiply(other.numerator), denominator.multiply(other.denominator));
    }

    public Fraction div(Fraction other){
        //TODO: Simplify before multiply
        return new Fraction(numerator.multiply(other.denominator), denominator.multiply(other.numerator));
    }

    private BigInteger getGcd(BigInteger b1, BigInteger b2) {
        return b1.gcd(b2);
    }

}
