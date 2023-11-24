package algonquin.cst2335.dictionaryapi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {

    private List<Definition> definitions;

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.definition_item, parent, false);

        return new DefinitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
        Definition definition = definitions.get(position);
        holder.wordTextView.setText(definition.getWord());
        holder.meaningTextView.setText(definition.getDefinition());
    }

    @Override
    public int getItemCount() {
        return definitions != null ? definitions.size() : 0;
    }

    static class DefinitionViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView meaningTextView;

        DefinitionViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            meaningTextView = itemView.findViewById(R.id.meaningTextView);
        }
    }
}
