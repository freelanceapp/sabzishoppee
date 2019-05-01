package ibt.pahadisabzi.model;

import java.util.ArrayList;
import java.util.Collections;

import ibt.pahadisabzi.model.productlist_responce.Product;

public class PriceProductSorter {

    ArrayList<Product> jobCandidate = new ArrayList<>();
    public PriceProductSorter(ArrayList<Product> jobCandidate) {
        this.jobCandidate = jobCandidate;
    }
    public ArrayList<Product> getSortedJobCandidateByAge() {
        Collections.sort(jobCandidate);
        return jobCandidate;
    }

    public ArrayList<Product> getSortedJobCandidateByName() {
        Collections.sort(jobCandidate, Product.nameComparator);
        return jobCandidate;
    }


    public ArrayList<Product> getSortedProductByHightoLow() {
        Collections.sort(jobCandidate, Product.hightolowComparator);
        return jobCandidate;
    }

}
