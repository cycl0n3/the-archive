import { action, configure, IObservableArray, observable, makeObservable } from 'mobx';
import { enableStaticRendering } from 'mobx-react';
import { io, Socket } from 'socket.io-client';

import { addTeamApiMethod, getTeamInvitationsApiMethod } from '../api/team-leader';
import { getTeamMembersApiMethod } from '../api/team-member';

import { User } from './user';
import { Team } from './team';

const dev = process.env.NODE_ENV !== 'production';

enableStaticRendering(typeof window === 'undefined');

configure({ enforceActions: 'observed' });

class Store {
  public isServer: boolean;

  public currentUser?: User = null;
  public currentUrl = '';

  public currentTeam?: Team = null;

  public teams: IObservableArray<Team> = observable([]);

  public socket: Socket;

  constructor({
    initialState = {},
    isServer,
    socket = null,
  }: {
    initialState?: any;
    isServer: boolean;
    socket?: Socket;
  }) {
    makeObservable(this, {
      currentUser: observable,
      currentUrl: observable,
      currentTeam: observable,

      changeCurrentUrl: action,
      setCurrentUser: action,
      setCurrentTeam: action,
    });

    this.isServer = !!isServer;

    // console.log('initialState.user', initialState.user);

    this.setCurrentUser(initialState.user);

    this.currentUrl = initialState.currentUrl || '';

    // console.log(initialState);

    this.setCurrentTeam(initialState.team);

    if (initialState.teams && initialState.teams.length > 0) {
      this.setInitialTeamsStoreMethod(initialState.teams);
    }

    this.socket = socket;

    if (socket) {
      socket.on('disconnect', () => {
        console.log('socket: ## disconnected');
      });

      socket.on('reconnect', (attemptNumber) => {
        console.log('socket: $$ reconnected', attemptNumber);
      });
    }
  }

  public changeCurrentUrl(url: string) {
    this.currentUrl = url;
  }

  public async setCurrentUser(user) {
    if (user) {
      this.currentUser = new User({ store: this, ...user });
    } else {
      this.currentUser = null;
    }
  }

  public async addTeam({ name, avatarUrl }: { name: string; avatarUrl: string }): Promise<Team> {
    const data = await addTeamApiMethod({ name, avatarUrl });
    const team = new Team({ store: this, ...data });

    return team;
  }

  public async setCurrentTeam(team) {
    if (this.currentTeam) {
      if (this.currentTeam.slug === team.slug) {
        return;
      }
    }

    if (team) {
      this.currentTeam = new Team({ ...team, store: this });

      const users =
        team.initialMembers || (await getTeamMembersApiMethod(this.currentTeam._id)).users;

      const invitations =
        team.initialInvitations ||
        (await getTeamInvitationsApiMethod(this.currentTeam._id)).invitations;

      this.currentTeam.setInitialMembersAndInvitations(users, invitations);
    } else {
      this.currentTeam = null;
    }
  }

  private setInitialTeamsStoreMethod(teams: any[]) {
    // console.log(initialTeams);

    const teamObjs = teams.map((t) => new Team({ store: this, ...t }));

    this.teams.replace(teamObjs);
  }
}

let store: Store = null;

function initializeStore(initialState = {}) {
  const isServer = typeof window === 'undefined';

  const socket = isServer
    ? null
    : io(dev ? process.env.NEXT_PUBLIC_URL_API : process.env.NEXT_PUBLIC_PRODUCTION_URL_API, {
        reconnection: true,
        autoConnect: true,
        transports: ['polling', 'websocket'],
        withCredentials: true,
      });

  const _store =
    store !== null && store !== undefined ? store : new Store({ initialState, isServer, socket });

  // For SSG and SSR always create a new store
  if (typeof window === 'undefined') {
    return _store;
  }
  // Create the store once in the client
  if (!store) {
    store = _store;
  }

  // console.log(_store);

  return _store;
}

function getStore() {
  return store;
}

export { Store, initializeStore, getStore };