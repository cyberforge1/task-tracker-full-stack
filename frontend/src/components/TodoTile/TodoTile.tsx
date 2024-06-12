// TodoTile.tsx

import React, { useState } from "react";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import { Todo } from "../../types/types";
import Modal from "../Modal/Modal";
import "./TodoTile.scss";

dayjs.extend(relativeTime);

interface TodoTileProps {
  todo: Todo;
  onDelete: (id: number) => void;
  onUpdate: (id: number, data: Partial<Todo>) => void;
}

const TodoTile: React.FC<TodoTileProps> = ({ todo, onDelete, onUpdate }) => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  const handleEditSave = (title: string, description: string) => {
    onUpdate(todo.id, { title, description });
    setIsEditModalOpen(false);
  };

  const handleToggleCompleted = () => {
    onUpdate(todo.id, { completed: !todo.completed });
  };

  return (
    <>
      <div className={`todo-tile ${todo.completed ? "completed" : ""}`}>
        <h2 className={todo.completed ? "line-through" : ""}>{todo.title}</h2>
        <p className={todo.completed ? "line-through" : ""}>{todo.description}</p>
        <h4 className={todo.completed ? "line-through" : ""}>{dayjs(todo.createdAt).fromNow()}</h4>
        <div className="actions">
          <button onClick={handleToggleCompleted} className={`completed-btn ${!todo.completed ? "completed" : ""}`}>
            {todo.completed ? "Incomplete" : "Complete"}
          </button>
          <button onClick={() => setIsEditModalOpen(true)} className="edit-btn">
            Edit
          </button>
          <button onClick={() => onDelete(todo.id)} className="delete-btn">
            Delete
          </button>
        </div>
      </div>
      <Modal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        onSave={handleEditSave}
        initialTitle={todo.title}
        initialDescription={todo.description}
      />
    </>
  );
};

export default TodoTile;
