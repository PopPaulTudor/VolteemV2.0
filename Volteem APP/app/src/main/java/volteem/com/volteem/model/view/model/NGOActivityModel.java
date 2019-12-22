package volteem.com.volteem.model.view.model;

import volteem.com.volteem.model.entity.NGO;

public class NGOActivityModel {
    private NGO ngo;

    public NGOActivityModel(NGO ngo) {
        this.ngo = ngo;
    }

    public NGO getNgo() {
        return ngo;
    }

    public void setNgo(NGO ngo) {
        this.ngo = ngo;
    }
}
