import React, { useEffect, useState } from "react";
import { deleteTodo, updateTodo } from "../../services/todo-services";
import TodoCard from "../../components/TodoCard/TodoCard";
import { Todo } from "../../types/types";
import "./AllTodos.scss";

interface AllTodosProps {
  todos: Todo[];
  setTodos: React.Dispatch<React.SetStateAction<Todo[]>>;
  filter: string;
}

const AllTodos: React.FC<AllTodosProps> = ({ todos, setTodos, filter }) => {
  const [filteredTodos, setFilteredTodos] = useState<Todo[]>([]);

  useEffect(() => {
    let filtered: Todo[] = [];
    if (filter === "completed") {
      filtered = todos.filter(todo => todo.completed);
    } else if (filter === "incomplete") {
      filtered = todos.filter(todo => !todo.completed);
    } else {
      filtered = todos;
    }
    setFilteredTodos(filtered.reverse()); // Reverse the filtered todos array here
  }, [todos, filter]);

  const handleDelete = (id: number) => {
    deleteTodo(id)
      .then(() => setTodos(todos.filter(todo => todo.id !== id)))
      .catch((e) => console.error(e));
  };

  const handleUpdate = (id: number, data: Partial<Todo>) => {
    updateTodo(id, data)
      .then((updatedTodo) => setTodos(todos.map(todo => (todo.id === id ? updatedTodo : todo))))
      .catch((e) => console.error(e));
  };

  return (
    <div className="todo-list">
      {filteredTodos.map((todo) => (
        <TodoCard key={todo.id} todo={todo} onDelete={handleDelete} onUpdate={handleUpdate} />
      ))}
    </div>
  );
};

export default AllTodos;
