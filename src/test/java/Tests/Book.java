package Tests;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
class Book {
    @JsonProperty
    private Integer id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String author;
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id)
    {
    	this.id = id;
    }
    
    
}

