package q11.confluence;

import java.util.ArrayList;
import java.util.List;

public class ConfluencePage implements ConfluenceNode {
    private String content;
    private String title;
    private String status;
    private List<ConfluenceAuthor> contributors = new ArrayList<>();
    List<ConfluenceNode> confluenceNodes = new ArrayList<>();
    int iterator = 0;

    public void addSubnode(ConfluenceNode node) {
        this.confluenceNodes.add(node);
    }

    public int getNumPages() {
        return confluenceNodes.size();
    }

    public boolean hasnext() {
        return iterator < confluenceNodes.size();
    }

    public ConfluenceNode next() {
        return confluenceNodes.get(iterator);
    }

    public ConfluencePage() {
        this.status = "Editing";
    }

    public String interact(String using, ConfluenceAuthor interactor) {
        switch (status) {
            case "Editing":
                String content = using;
                if (content.isEmpty()) {
                    throw new IllegalArgumentException("Document content cannot be empty");
                }

                if (!contributors.contains(interactor)) {
                    contributors.add(interactor);
                }

                this.content = content;

                return "";
            case "Viewing":
                String result =
                    "==== Draft in Progress === \n" +
                    this.content.length() + " characters long \n" + 
                    "=============================\n" + this.content;
                return result;
            case "Publishing":
                String title = using;
                if (title.isEmpty()) {
                    throw new IllegalArgumentException("Title cannot be empty");
                } else if (title.length() > 50) {
                    throw new IllegalArgumentException("Title cannot be > 50 characters long");
                }

                this.title = title;
                return "";
            case "Published":
            String result2 =
                "==== " + this.title + " === \n" +
                "By " + getContributors() + "\n" + 
                "=============================\n" + this.content;
                return result2;
            default:
                return null;
        }
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    private String getContributors() {
        String result = "";
        for (ConfluenceAuthor contributor : contributors) {
            result += contributor.getName() + ", ";
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }
}
