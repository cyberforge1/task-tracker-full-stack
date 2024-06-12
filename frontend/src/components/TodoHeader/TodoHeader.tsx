// TodoHeader.tsx

import React from "react";
import HeaderControls from "../HeaderControls/HeaderControls";
import AddTaskForm from "../AddTaskForm/AddTaskForm";
import "./TodoHeader.scss";

interface TodoHeaderProps {
  filter: string;
  onFilterChange: (filter: string) => void;
  onAddTask: (title: string, description: string) => void;
  onSearch: (searchQuery: string) => void;
  viewMode: string;
  onViewModeChange: (viewMode: string) => void;
}

const TodoHeader: React.FC<TodoHeaderProps> = ({
  filter,
  onFilterChange,
  onAddTask,
  onSearch,
  viewMode,
  onViewModeChange,
}) => {
  const [searchQuery, setSearchQuery] = React.useState("");

  const handleSearch = (query: string) => {
    setSearchQuery(query);
    onSearch(query);
  };

  return (
    <div className="header">
      <HeaderControls
        filter={filter}
        onFilterChange={onFilterChange}
        searchQuery={searchQuery}
        onSearch={handleSearch}
        viewMode={viewMode}
        onViewModeChange={onViewModeChange}
      />
      <AddTaskForm onAddTask={onAddTask} />
    </div>
  );
};

export default TodoHeader;
