import { create } from "zustand";
import { createSelectors } from "./create-selectors";

// type User = {
//   username:
// }

interface UserState {
  user: any;
  setUser: (data: any) => void;
}

export enum ROLE {
  ADMIN = 1,
  USER = 2,
}

const useUserStoreBase = create<UserState>()((set) => ({
  user: {},
  setUser: (user) => set({ user }),
}));

export const useUserStore = createSelectors(useUserStoreBase);
