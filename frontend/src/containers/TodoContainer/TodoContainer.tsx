// TodoContainer.tsx

import React, { useEffect, useState } from "react";
import AllTodos from "../../pages/AllTodos/AllTodos";
import { createTodo, getAllTodos } from "../../services/todo-services";
import { Todo } from "../../types/types";
import Modal from "../../components/Modal/Modal";
import TodoHeader from "../../components/TodoHeader/TodoHeader";
import "./TodoContainer.scss";

const TodoContainer: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [filter, setFilter] = useState<string>("incomplete");
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [viewMode, setViewMode] = useState<string>("card");
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [newTask, setNewTask] = useState<string>("");

  useEffect(() => {
    getAllTodos()
      .then((data) => setTodos(data))
      .catch((e) => console.error(e));
  }, []);

  const handleCreate = (title: string, description: string) => {
    const newTodo = { title, description };
    createTodo(newTodo)
      .then((createdTodo) => setTodos([...todos, createdTodo]))
      .catch((e) => console.error(e));
  };

  const getFilteredTodos = () => {
    return todos.filter((todo) => {
      const matchesFilter =
        filter === "all" ||
        (filter === "completed" && todo.completed) ||
        (filter === "incomplete" && !todo.completed);
      const matchesSearchQuery =
        todo.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (todo.description && todo.description.toLowerCase().includes(searchQuery.toLowerCase()));
      return matchesFilter && matchesSearchQuery;
    });
  };

  return (
    <div className="todo-container">
      <TodoHeader
        filter={filter}
        onFilterChange={setFilter}
        onAddTask={handleCreate}
        onSearch={setSearchQuery}
        viewMode={viewMode}
        onViewModeChange={setViewMode}
      />
      <AllTodos todos={getFilteredTodos()} setTodos={setTodos} filter={filter} viewMode={viewMode} />
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleCreate}
        initialTitle={newTask}
        initialDescription=""
      />
    </div>
  );
};

export default TodoContainer;
