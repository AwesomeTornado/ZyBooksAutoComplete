public class ZyBooksActivityMetadata {
    private String activity_type;
    private long canonical_section_id;
    private String caption;
    private long content_resource_id;
    private Object optional;
    private int parts;
    private String payload;
    private String resource_type;
    private String tool;

    public String toString(){
        StringBuilder message = new StringBuilder();
        message.append("This is a ");
        message.append(activity_type);
        message.append(" activity intended to teach ");
        message.append(caption);
        message.append("\nIt has ");
        message.append(parts);
        message.append(" parts, with ");
        message.append(resource_type);
        message.append(" format answers. ");
        if (tool != null){
            message.append(tool);
            message.append(" is used to complete this problem.");
        }
        return message.toString();
    }
}
