import React, { useEffect, useState } from "react";
import AllTodos from "../../pages/AllTodos/AllTodos";
import { createTodo, getAllTodos } from "../../services/todo-services";
import { Todo } from "../../types/types";
import Modal from "../../components/Modal/Modal";
import "./TodoContainer.scss";

const TodoContainer: React.FC = () => {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [filter, setFilter] = useState<string>("all");
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

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

  return (
    <div className="todo-container">
      <div className="header">
        <h1>TODO LIST</h1>
        <button className="add-task-btn" onClick={() => setIsModalOpen(true)}>Add Task</button>
        <select onChange={(e) => setFilter(e.target.value)}>
          <option value="all">All</option>
          <option value="completed">Completed</option>
          <option value="incomplete">Incomplete</option>
        </select>
      </div>
      <AllTodos todos={todos} setTodos={setTodos} filter={filter} />
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSave={handleCreate}
      />
    </div>
  );
};

export default TodoContainer;
